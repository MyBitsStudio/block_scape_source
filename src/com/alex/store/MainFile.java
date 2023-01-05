package com.alex.store;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*
 * Created by Alex(Dragonkk)
 * 23/10/11
 */
public final class MainFile {

	private static final int IDX_BLOCK_LEN = 6;
	private static final int HEADER_LEN = 8;
	private static final int EXPANDED_HEADER_LEN = 10;
	private static final int BLOCK_LEN = 512;
	private static final int EXPANDED_BLOCK_LEN = 510;
	private static final int TOTAL_BLOCK_LEN = HEADER_LEN + BLOCK_LEN;
	private static final ByteBuffer tempBuffer = ByteBuffer.allocateDirect(TOTAL_BLOCK_LEN);

	private final int id;
	private final FileChannel index;
	private final FileChannel data;
	private final boolean newProtocol;

	MainFile(int id, @NotNull RandomAccessFile data, @NotNull RandomAccessFile index, boolean newProtocol) {
		this.id = id;
		this.data = data.getChannel();
		this.index = index.getChannel();
		this.newProtocol = newProtocol;
	}

	public Archive getArchive(int id) {
		return getArchive(id, null);
	}

	@Nullable
	public Archive getArchive(int id, int[] keys) {
		byte[] data = getArchiveData(id);
		if (data == null) {
			return null;
		}
		return new Archive(id, data, keys);
	}

	@Nullable
	public byte[] getArchiveData(int archiveId) {
		synchronized (data) {
			try {
				tempBuffer.position(0).limit(IDX_BLOCK_LEN);
				index.read(tempBuffer, (long) archiveId * (long) IDX_BLOCK_LEN);
				tempBuffer.flip();
				int size = getMediumInt();
				int block = getMediumInt();
				if (size < 0) {
					return null;
				}
				if (block <= 0 || block > data.size() / TOTAL_BLOCK_LEN) {
					return null;
				}
				ByteBuffer fileBuffer = ByteBuffer.allocate(size);
				int remaining = size;
				int chunk = 0;
				int blockLen = !newProtocol || archiveId <= 0xffff ? BLOCK_LEN : EXPANDED_BLOCK_LEN;
				int headerLen = !newProtocol || archiveId <= 0xffff ? HEADER_LEN : EXPANDED_HEADER_LEN;
				while (remaining > 0) {
					if (block == 0) {
						System.out.println(archiveId + ", " + newProtocol);
						return null;
					}
					int blockSize = Math.min(remaining, blockLen);
					tempBuffer.position(0).limit(blockSize + headerLen);
					data.read(tempBuffer, (long) block * (long) TOTAL_BLOCK_LEN);
					tempBuffer.flip();

					int currentFile, currentChunk, nextBlock, currentIndex;

					if (!newProtocol || archiveId <= 65535) {
						currentFile = tempBuffer.getShort() & 0xffff;
						currentChunk = tempBuffer.getShort() & 0xffff;
						nextBlock = getMediumInt();
						currentIndex = tempBuffer.get() & 0xff;
					} else {
						currentFile = tempBuffer.getInt();
						currentChunk = tempBuffer.getShort() & 0xffff;
						nextBlock = getMediumInt();
						currentIndex = tempBuffer.get() & 0xff;
					}

					if (archiveId != currentFile && archiveId <= 65535 || chunk != currentChunk || id != currentIndex) {
						return null;
					}
					if (nextBlock < 0 || nextBlock > data.size() / TOTAL_BLOCK_LEN) {
						return null;
					}

					fileBuffer.put(tempBuffer);
					remaining -= blockSize;
					block = nextBlock;
					chunk++;
				}
				return (byte[]) fileBuffer.flip().array();
			} catch (Exception ex) {
				return null;
			}
		}
	}

	private static int getMediumInt() {
		return (MainFile.tempBuffer.get() & 0xff) << 16 | (MainFile.tempBuffer.get() & 0xff) << 8 | MainFile.tempBuffer.get() & 0xff;
	}

	private static void putMediumInt(int val) {
		MainFile.tempBuffer.put((byte) (val >> 16));
		MainFile.tempBuffer.put((byte) (val >> 8));
		MainFile.tempBuffer.put((byte) val);
	}

	public boolean putArchive(@NotNull Archive archive) {
		return putArchiveData(archive.getId(), archive.getData());
	}

	public boolean putArchiveData(int id, byte[] archive) {
		ByteBuffer buffer = ByteBuffer.wrap(archive);
		boolean done = putArchiveData(id, buffer, archive.length, true);
		if (!done) {
			done = putArchiveData(id, buffer, archive.length, false);
		}
		return done;
	}

	public boolean putArchiveData(int archiveId, ByteBuffer archive, int size, boolean exists) {
		synchronized (data) {
			try {
				int block;
				if (exists) {
					if ((long) archiveId * IDX_BLOCK_LEN + IDX_BLOCK_LEN > index.size()) {
						return false;
					}

					tempBuffer.position(0).limit(IDX_BLOCK_LEN);
					index.read(tempBuffer, (long) archiveId * IDX_BLOCK_LEN);
					tempBuffer.flip().position(3);
					block = getMediumInt();

					if (block <= 0 || block > data.size() / TOTAL_BLOCK_LEN) {
						return false;
					}
				} else {
					block = (int) (data.size() + TOTAL_BLOCK_LEN - 1) / TOTAL_BLOCK_LEN;
					if (block == 0) {
						block = 1;
					}
				}

				tempBuffer.position(0);
				putMediumInt(size);
				putMediumInt(block);
				tempBuffer.flip();
				index.write(tempBuffer, (long) archiveId * IDX_BLOCK_LEN);

				int remaining = size;
				int chunk = 0;
				int blockLen = !newProtocol || archiveId <= 0xffff ? BLOCK_LEN : EXPANDED_BLOCK_LEN;
				int headerLen = !newProtocol || archiveId <= 0xffff ? HEADER_LEN : EXPANDED_HEADER_LEN;
				while (remaining > 0) {
					int nextBlock = 0;
					if (exists) {
						tempBuffer.position(0).limit(headerLen);
						data.read(tempBuffer, (long) block * TOTAL_BLOCK_LEN);
						tempBuffer.flip();

						int currentFile, currentChunk, currentIndex;
						if (!newProtocol || archiveId <= 0xffff) {
							currentFile = tempBuffer.getShort() & 0xffff;
						} else {
							currentFile = tempBuffer.getInt();
						}
						currentChunk = tempBuffer.getShort() & 0xffff;
						nextBlock = getMediumInt();
						currentIndex = tempBuffer.get() & 0xff;

						if (archiveId != currentFile && archiveId <= 65535 || chunk != currentChunk || id != currentIndex) {
							return false;
						}
						if (nextBlock < 0 || nextBlock > data.size() / TOTAL_BLOCK_LEN) {
							return false;
						}
					}

					if (nextBlock == 0) {
						exists = false;
						nextBlock = (int) ((data.size() + TOTAL_BLOCK_LEN - 1) / TOTAL_BLOCK_LEN);
						if (nextBlock == 0) {
							nextBlock = 1;
						}
						if (nextBlock == block) {
							nextBlock++;
						}
					}

					if (remaining <= blockLen) {
						nextBlock = 0;
					}
					tempBuffer.position(0).limit(TOTAL_BLOCK_LEN);
					if (!newProtocol || archiveId <= 0xffff) {
						tempBuffer.putShort((short) archiveId);
						tempBuffer.putShort((short) chunk);
						putMediumInt(nextBlock);
						tempBuffer.put((byte) id);
					} else {
						tempBuffer.putInt(archiveId);
						tempBuffer.putShort((short) chunk);
						putMediumInt(nextBlock);
						tempBuffer.put((byte) id);
					}

					int blockSize = Math.min(remaining, blockLen);
					archive.limit(archive.position() + blockSize);
					tempBuffer.put(archive);
					tempBuffer.flip();

					data.write(tempBuffer, (long) block * TOTAL_BLOCK_LEN);
					remaining -= blockSize;
					block = nextBlock;
					chunk++;
				}

				return true;
			} catch (Exception ex) {
				return false;
			}
		}
	}

	public int getId() {
		return id;
	}

	public int getArchivesCount() throws IOException {
		synchronized (index) {
			return (int) (index.size() / 6);
		}
	}
}
