import java.security.MessageDigest;
import java.util.Random;

/**
 * Created by Plagueis on 10/7/2015.
 */
public class HashGen {
    public static void main(String[] args) throws Exception {
        if (args.length == 0){
            System.out.println("Usage: <hash algorithm / help> <string>");
        } else if (args[0].equalsIgnoreCase("help")) {
            System.out.println("Usage: <hash algorithm / help> <string>");
            System.out.println("Available Algorithms");
            System.out.println("MD2");
            System.out.println("MD5");
            System.out.println("SHA1");
            System.out.println("SHA256");
            System.out.println("SHA384");
            System.out.println("SHA512");
            System.out.println("vb3 - vBulletin < v3.8.5");
            System.out.println("vb4 - vBulletin > v3.8.5");
            System.out.println("ib - IPB / MyBB");
        } else if (args.length == 2) {
            String algorithm = args[0];
            if(algorithm.equalsIgnoreCase("sha256") || algorithm.equalsIgnoreCase("sha384") || algorithm.equalsIgnoreCase("sha512")){
                algorithm = algorithm.replace("sha", "sha-");
            }
            String password = args[1];
            try {
                System.out.println("MD5(" + password + ") is " + hash(password, algorithm));
            } catch (Exception e) {
                if(algorithm.equalsIgnoreCase("vb3")){
                    String salt = generateString(3);
                    String stage1 = hash(password, "MD5");
                    String stage2 = hash(stage1 + salt, "MD5");
                    System.out.println("md5(md5(" + password + ").$salt) is " + stage2 + ":" + salt);
                } else if(algorithm.equalsIgnoreCase("vb4")){
                    String salt = generateString(30);
                    String stage1 = hash(password, "MD5");
                    String stage2 = hash(stage1 + salt, "MD5");
                    System.out.println("md5(md5(" + password + ").$salt) is " + stage2 + ":" + salt);
                } else if(algorithm.equalsIgnoreCase("ib")){
                    String salt = generateString(5);
                    String stage1 = hash(salt, "MD5");
                    String stage2 = hash(password, "MD5");
                    String stage3 = hash(stage1 + stage2, "MD5");
                    System.out.println("md5(md5($salt).md5("+password+")) is " + stage3 + ":" + salt);
                } else {
                    System.out.println("Invalid Algorithm!");
                    System.exit(0);
                }
            }
        }
    }

    public static String hash(String password, String algorithm) throws Exception{
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static String generateString(int length) {
        Random random = new Random();
        char[] text = new char[length];

        for (int i = 0; i < length; i++) text[i] = (char) (random.nextInt(93) + 33);

        return new String(text);
    }
}
