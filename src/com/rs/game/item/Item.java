package com.rs.game.item;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ItemsEquipIds;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a single item.
 */
public class Item implements Serializable {

	/**
	 * New Item classes for Blockchain
	 * @author Corrupt
	 */

	@Serial
	private static final long serialVersionUID = -6485003878697568087L;

	private int newId;
	protected int charges;

	protected int amount;

	//private final String hash;
	private String signature;
	private String[] signatures;
	private final long dealt;

	public Item(int id) {
		this(id, 1);
	}

	public Item(int id, int amount) {
		this(id, amount, false,0);
	}
	public Item(int id, int amount, int charges) {
		this(id, amount, false, charges);
	}

	public Item(int id, int amount, boolean amt0, int charges) {
		this.newId = id;
		this.amount = amount;
		if (this.amount <= 0 && !amt0) {
			this.amount = 1;
		}
		this.charges = charges;
		//this.hash = ChainHelper.createRandomString(32);
		this.dealt = System.currentTimeMillis();
		this.signatures = new String[36];
		//generateSignature();
	}

	
	public Item(@NotNull Item item) {
		this.newId =  item.getId();
		this.amount = item.getAmount();
		this.charges = item.getCharges();
		//this.hash = item.getHash();
		this.dealt = item.getDealt();
		this.signatures = item.getSignatures();
		this.signature = item.getSignature();
	}

	@Override
	public Item clone() throws CloneNotSupportedException {
		Item clone = (Item) super.clone();
		return new Item(newId, amount);
	}

	public int getAmount() {
		return amount;
	}

	public ItemDefinitions getDefinitions() {
		return ItemDefinitions.getItemDefinitions(newId);
	}
	
	public int getId() {
		return newId;
	}

	public String getName() {
		return getDefinitions().getName();
	}

	public void setAmount(int amount) {
		if (this.amount + amount < 0)
			return;
		this.amount = amount;
	}

	public void setId(int id) {
		this.newId = id;
	}
	
	public int getEquipId() {
		  return ItemsEquipIds.getEquipId(newId);
	}

	public int getCharges() {
		return charges;
	}
	public void setCharges(int charges) {
		if (this.charges + charges < 0)
			return;
		this.charges = charges;
	}

//	public String getHash(){
//		return hash;
//	}

	public String getSignature(){
		return signature;
	}

	public void setSignature(String signature){
		this.signature = signature;
	}

	public String[] getSignatures(){
		return signatures;
	}

	public long getDealt(){
		return dealt;
	}

//	public boolean containsSignature(String signature){ // Checks for the current owner of the item
//		String s = this.signatures[this.signatures.length - 1];
//		return s != null && s.equals(signature);
//	}
//
//	public void addToSignatures(String signature){
//		for(int i = 0; i < signatures.length; i++){
//			if(signatures[i] == null){
//				signatures[i] = signature;
//				break;
//			}
//		}
//	}
//
//	public void deconstructSignatures(){
//		if(signatures.length >= 32){
//			StringBuilder sb = new StringBuilder();
//			for(String s: signatures){
//				sb.append(s).append(":");
//			}
//			signatures = new String[36];
//			signatures[0] = ChainHelper.applySha256(sb.toString());
//		}
//	}
//
//	public void generateSignature(){
////		MasterChain chain = MasterChain.singleton;
//		StringBuilder sb = new StringBuilder();
//		sb.append(newId).append(":").append(amount).append(":").append(charges).append(":").append(hash).append(":").append(dealt).append(":");
////		try {
////			this.signature = Arrays.toString(chain.getSecurityManager().getSignatures().sign(sb.toString().getBytes()));
////		} catch (Exception e) {
////			ChainLogger.logChain("Error generating signature for item: " + newId , ChainLogger.ERRORS , true);
////		}
//		try {
//			MessageDigest md = MessageDigest.getInstance("SHA-256");
//
//			// digest() method is called
//			// to calculate message digest of the input string
//			// returned as array of byte
//			byte[] messageDigest = md.digest(sb.toString().getBytes());
//
//			// Convert byte array into signum representation
//			BigInteger no = new BigInteger(1, messageDigest);
//
//			// Convert message digest into hex value
//			StringBuilder hashtext = new StringBuilder(no.toString(16));
//
//			// Add preceding 0s to make it 32 bit
//			while (hashtext.length() < 32) {
//				hashtext.insert(0, "0");
//			}
//
//			this.signature = hashtext.toString();
//		} catch (NoSuchAlgorithmException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	public void verifySignature(){
//		MasterChain chain = MasterChain.singleton;
//		StringBuilder sb = new StringBuilder();
//		sb.append(newId).append(":").append(amount).append(":").append(charges).append(":").append(hash).append(":").append(dealt).append(":");
//		try {
//			if(!chain.getSecurityManager().getSignatures().verify(sb.toString().getBytes(), signature.getBytes())){
//				ChainLogger.logChain("Signatures don't match. Deleting item : " + this.hash , ChainLogger.ERRORS , true);
//				chain.getSecurityManager().addToRemove(this);
//			}
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

	public void addAmount(int amount){
		this.amount += amount;
		//generateSignature();
	}

	public void removeAmount(int amount){
		this.amount -= amount;
		//generateSignature();
	}
}