package com.hyperledger;

import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BlockchainUtils {

    public static String mod = "26745649337183522936140983118491615357902963042762760512633525371178616311744381608586987059657097962539974445136175346458916649847784002633454648858356567623979951097604864390024014207078458013634896974827423696615470111783496403735000795924831125624307964238951562271158285949820627985939848320766761860603689675701129032540159752519545181498192588089542590015265783129060954383931706708652467117705443680525818868832983535702596187035051362996109826691895154498985374347207791932496711033893919388282841184672608274152972625655929134962717275468946069381703630955713457027196904276238285015992613239007291469707359";
    public static String privateExp = "9051861927207518355815418405781516920914394491203433320879318697765038056405484783698729976604425917544561136157330016206600862901090575864188791659715547678161760613400440889923671317272742694530247529642181840834634402160822771610604470055246910629262291542213789568570828104887261227125727538318311205107235300796307420998121291600753791058933439869844393307768621271082428180640772995907587717502899601165765714817404227562409381019190610977288898173488263260401078675881780062203544034772630495056133232266357465633198909627173232633049295343949037588607233638300948605292827884264353364734268650734350669494629";


    public static @Nullable PublicKey getPublicKey(String modulusStr, String exponentStr) {
        try {
            BigInteger modulus = new BigInteger(modulusStr);
            BigInteger exponent = new BigInteger(exponentStr);
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(publicKeySpec);
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static @Nullable PrivateKey getPrivateKey(String modulusStr, String exponentStr) {
        try {
            BigInteger modulus = new BigInteger(modulusStr);
            BigInteger exponent = new BigInteger(exponentStr);
            RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static @org.jetbrains.annotations.NotNull
    String createRandomString(int length){
        int leftLimit = 48;
        int rightLimit = 122;
        SecureRandom random = new SecureRandom();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static float getFromCoins(double amount){
        return  Float.parseFloat(String.format("%.6f", amount));
    }

    public static float coinToChain(int amount){
        return amount * 0.000001f;
    }

    public static String formatCoins(float amount){
        int position = String.valueOf(amount).length();
        String sb = "#" +
                "0".repeat(Math.max(0, position > 6 ? position - 6 : 0)) +
                ".000000";
        NumberFormat formatter = new DecimalFormat(sb);
        return formatter.format(amount);
    }

}
