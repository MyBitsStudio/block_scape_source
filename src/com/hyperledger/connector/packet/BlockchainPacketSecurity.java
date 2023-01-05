package com.hyperledger.connector.packet;

import com.hyperledger.BlockchainConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

public record BlockchainPacketSecurity(BlockchainPacket packet, boolean coming, int type) {

    private static final int[] ignoredAwaiting = {3};
    private static final int[] ignoredEncryption = {0};

    public static final int[] ignoredSerials = {3, 4};

    public BlockchainPacketSecurity {
        if (packet == null) {
            throw new IllegalArgumentException("Packet cannot be null");
        }

        if(packet.opcode == null){
            packet.properties.add(BlockchainPacketProperties.INVALID_OPCODE);
            throw new IllegalArgumentException("Opcode cannot be null");
        }

        switch(type){
            case 0 -> handleTCPPacket(packet, coming);
            case 1 -> handleUDPPacket(packet, coming);
        }


    }

    private void handleTCPPacket(@NotNull BlockchainPacket packet, boolean coming){
        if(packet.listData == null){
            packet.properties.add(BlockchainPacketProperties.INVALID_DATA);
            System.out.println("Packet data is null");
            return;
        }
        if(coming){
            if(Arrays.stream(ignoredEncryption).noneMatch(i -> i == packet.opcode.getOpcode())){
                if(!decryptTCP(packet)){
                    packet.properties.add(BlockchainPacketProperties.INVALID_PACKET);
                    System.out.println("Packet cant be decrypted");
                    return;
                }

                if (!verifyLength(packet)) {
                    packet.properties.add(BlockchainPacketProperties.INVALID_LENGTH);
                    System.out.println("Packet length is invalid Expected : " + packet.opcode.getLengths()[0] + " Actual : " + packet.listData.size());
                }

                if(Arrays.stream(ignoredSerials).noneMatch(i -> i == packet.opcode.getOpcode())){
                    if(!packet.listData.get(1).split(":")[0].equals(packet.connect.getSuperSerial("33306"))
                            || !packet.listData.get(packet.listData.size() - 1).split(":")[2].equals(packet.connect.getSuperSerial("33306"))){
                        packet.properties.add(BlockchainPacketProperties.INVALID_SUPER);
                        System.out.println("Super is invalid");
                    }

                    if(!packet.listData.get(1).split(":")[2].equals(packet.connect.getSerial("33306"))
                            || !packet.listData.get(packet.listData.size() - 1).split(":")[0].equals(packet.connect.getSerial("33306"))){
                        packet.properties.add(BlockchainPacketProperties.INVALID_SERIAL);
                        System.out.println("Serial is invalid");
                    }
                }

                if(Arrays.stream(ignoredAwaiting).noneMatch(i -> i == packet.opcode.getOpcode())) {

                    System.out.println("Security :: " + packet.listData);
                    String awaiting = packet.listData.get(2).split(":")[packet.listData.get(2).split(":").length - 1];
                    Optional<Map<String, String>> option = packet.connect.getOutgoing(awaiting);
                    option.ifPresent(stringStringMap -> System.out.println("Found : " + stringStringMap));

                    if (option.isEmpty()) {
                        packet.properties.add(BlockchainPacketProperties.INVALID_PACKET);
                        System.out.println("Awaiting not found");
                    } else {
                        Map<String, String> map = option.get();

                        String radix = new BigInteger(map.get("radix")).toString(6);

                        if(!packet.listData.get(1).split(":")[1].equals(radix)){
                            packet.properties.add(BlockchainPacketProperties.INVALID_RADIX);
                            System.out.println("Radix is invalid");
                        }

                    }
                }
            }
        } else if (!encryptTCP(packet)) {
            packet.properties.add(BlockchainPacketProperties.INVALID_PACKET);
        }
    }

    private void handleUDPPacket(@NotNull BlockchainPacket packet, boolean coming){
        if (packet.data == null) {
            packet.properties.add(BlockchainPacketProperties.INVALID_DATA);
            System.out.println("Packet data is null");
        }
        if(coming){
            if(Arrays.stream(ignoredEncryption).noneMatch(i -> i == packet.opcode.getOpcode())){
                if(!decryptUDP(packet)){
                    packet.properties.add(BlockchainPacketProperties.INVALID_PACKET);
                    System.out.println("Packet cant be decrypted");
                    return;
                }

                if(Arrays.stream(ignoredSerials).noneMatch(i -> i == packet.opcode.getOpcode())){
                    if(!packet.data[1].equals(packet.connect.getSerial(String.valueOf(packet.getPort())))){
                        packet.properties.add(BlockchainPacketProperties.INVALID_SERIAL);
                        System.out.println("Serial is invalid");
                    }
                }
            }
        } else if (!encryptUDP(packet)) {
            packet.properties.add(BlockchainPacketProperties.INVALID_PACKET);
            System.out.println("Can't encrypt packet");
        }
    }

    private boolean verifyLength(@NotNull BlockchainPacket packet){
        return packet.listData.size() == packet.opcode.getLengths()[0];
    }


    private boolean verifyHeader(@NotNull BlockchainPacket packet){
        return packet.listData.get(1).equals(packet.connect.certified(false, "33306"));
    }

    private boolean verifyFooter(@NotNull BlockchainPacket packet){
        return packet.listData.get(packet.listData.size() - 1).equals(packet.connect.certify("33306"));
    }

    private boolean decryptTCP(@NotNull BlockchainPacket packet){
        System.out.println("Decrypting...");
        List<String> decrypted = new ArrayList<>();
        for(int i = 1; i < packet.getPayload().size(); i++){
            decrypted.add(packet.getPayload().get(i));
        }
        packet.listData = new ArrayList<>(decryptedArrayList(decrypted));
        return true;
    }

    private boolean decryptUDP(@NotNull BlockchainPacket packet){
        System.out.println("Decrypting...");
        String[] decrypted = new String[packet.data.length - 1];
        for(int i = 1; i < packet.data.length; i++){
            decrypted[i - 1] = decrypt(packet.data[i].getBytes());
        }
        packet.data = decrypted;
        return true;
    }

    private boolean encryptUDP(@NotNull BlockchainPacket packet){
        System.out.println("Encrypting...");
        String[] encrypted = new String[packet.data.length + 1];

        return true;
    }

    private boolean encryptTCP(@NotNull BlockchainPacket packet){
        List<String> encrypted = new ArrayList<>();
        String opcode = packet.listData.get(0);
        for(int i = 1; i < packet.listData.size(); i++){
            encrypted.add(packet.listData.get(i));
        }
        encrypted = encryptArrayList(encrypted);
        List<String> packets = new ArrayList<>();
        packets.add(opcode);
        packets.addAll(encrypted);
        packet.listData = packets;
        return true;
    }

    public @NotNull ArrayList<String> encryptArrayList(@NotNull List<String> info){
        ArrayList<String> encrypted = new ArrayList<>();
        for(String s : info){
            encrypted.add(encrypt(s));
        }
        return encrypted;
    }

    public static StringBuilder byteToString(byte[] a) {
        if (a == null) {
            return null;
        } else {
            StringBuilder ret = new StringBuilder();

            for (byte b : a) {
                if (b != 0) {
                    ret.append((char) b);
                }
            }

            return ret;
        }
    }

    public static @NotNull String reverseString(String packet) {
        return (new StringBuilder()).append(packet).reverse().toString();
    }

    public @NotNull String encrypt(String content){
        BlockchainConnector connect = BlockchainConnector.singleton();
        String first = reverseString(content);
        String second = encrypt(first, connect.getPublic());
        return reverseString(second);
    }

    public static @Nullable String encrypt(String content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(1, publicKey);
            int splitLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8 - 11;
            byte[][] arrays = splitBytes(content.getBytes(), splitLength);
            StringBuilder sb = new StringBuilder();

            for (byte[] array : arrays) {
                sb.append(bytesToHexString(cipher.doFinal(array)));
            }

            return sb.toString();
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        }
    }

    public static @NotNull String bytesToHexString(byte @NotNull [] bytes) {

        StringBuilder sb = new StringBuilder(bytes.length);
        int var4 = bytes.length;

        for (byte aByte : bytes) {
            String sTemp = Integer.toHexString(255 & aByte);
            if (sTemp.length() < 2) {
                sb.append(0);
            }

            sb.append(sTemp.toUpperCase());
        }

        return sb.toString();
    }

    public @NotNull ArrayList<String> decryptedArrayList(@NotNull List<String> info){
        ArrayList<String> decrypted = new ArrayList<>();
        for(String s : info){
            decrypted.add(decrypt(s.getBytes()));
        }
        return decrypted;
    }

    public @Nullable String decrypt(byte[] packet) {
        BlockchainConnector connect = BlockchainConnector.singleton();
        String first = reverseString(byteToString(packet).toString());
        String second;
        try {
            second = decrypt(first, connect.getPrivate());
        } catch (Exception e) {
            return null;
        }
        return reverseString(second);
    }

    public static @Nullable String decrypt(String content, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, privateKey);
            int splitLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
            byte[] contentBytes = hexString2Bytes(content);
            byte[][] arrays = splitBytes(contentBytes, splitLength);
            StringBuilder sb = new StringBuilder();

            for (byte[] array : arrays) {
                sb.append(new String(cipher.doFinal(array)));
            }

            return sb.toString();
        } catch (Exception var11) {
            return null;
        }
    }

    public static byte[] @NotNull [] splitBytes(byte @NotNull [] bytes, int splitLength) {

        int y = bytes.length % splitLength;
        int x;
        if (y == 0) {
            x = bytes.length / splitLength;
        } else {
            x = bytes.length / splitLength + 1;
        }

        byte[][] arrays = new byte[x][];

        for (int i = 0; i < x; ++i) {
            byte[] array;
            if (i == x - 1 && bytes.length % splitLength != 0) {
                array = new byte[bytes.length % splitLength];
                System.arraycopy(bytes, i * splitLength, array, 0, bytes.length % splitLength);
            } else {
                array = new byte[splitLength];
                System.arraycopy(bytes, i * splitLength, array, 0, splitLength);
            }

            arrays[i] = array;
        }

        return arrays;
    }

    public static byte @NotNull [] hexString2Bytes(@NotNull String hex) {

        int len = hex.length() / 2;
        hex = hex.toUpperCase();
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();

        for (int i = 0; i < len; ++i) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }

        return result;
    }

    private static byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
