package sunwul.rsatest.tool;

import javafx.scene.control.Alert;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


public class RsaUtil {
    /**
     * 定义加密方式
     */
    private final static String KEY_RSA = "RSA";
    /**
     * 定义签名算法
     */
    private final static String KEY_RSA_SIGNATURE = "MD5withRSA";
    /**
     * 定义公钥算法
     */
    private final static String KEY_RSA_PUBLICKEY = "RSAPublicKey";
    /**
     * 定义私钥算法
     */
    private final static String KEY_RSA_PRIVATEKEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    private RsaUtil() {
    }

    /**
     * 创建密钥
     *
     * @return
     */
    public static Map<String, Object> generateKey() {
        Map<String, Object> map = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_RSA);
            generator.initialize(1024);
            KeyPair keyPair = generator.generateKeyPair();
            // 公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // 私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 将密钥封装为map
            map = new HashMap<>(2);
            map.put(KEY_RSA_PUBLICKEY, publicKey);
            map.put(KEY_RSA_PRIVATEKEY, privateKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     */
    public static String sign(String privateKey, byte[] data) {
        String str = "";
        try {
            // 解密由base64编码的私钥
            byte[] bytes = decryptBase64(privateKey);
            // 构造PKCS8EncodedKeySpec对象
            PKCS8EncodedKeySpec pkcs = new PKCS8EncodedKeySpec(bytes);
            // 指定的加密算法
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            // 取私钥对象
            PrivateKey key = factory.generatePrivate(pkcs);
            // 用私钥对信息生成数字签名
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initSign(key);
            signature.update(data);
            str = encryptBase64(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return str;
    }


    /**
     * 用私钥对信息生成数字签名
     *
     * @param privateKey
     * @param dataStr
     * @return
     */
    public static String sign(String privateKey, String dataStr) {
        String str = "";
        try {
            byte[] data = dataStr.getBytes("UTF-8");
            return sign(privateKey, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true，失败返回false
     */
    public static boolean verify(String publicKey, byte[] data, String sign) {
        boolean flag = false;
        try {
            // 解密由base64编码的公钥
            byte[] bytes = decryptBase64(publicKey);
            // 构造X509EncodedKeySpec对象
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            // 指定的加密算法
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            // 取公钥对象
            PublicKey key = factory.generatePublic(keySpec);
            // 用公钥验证数字签名
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initVerify(key);
            signature.update(data);
            flag = signature.verify(decryptBase64(sign));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return flag;
    }


    public static boolean verify(String publicKey, String dataStr, String sign) {

        try {
            byte[] data = dataStr.getBytes("UTF-8");
            return verify(publicKey, data, sign);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 公钥加密
     *
     * @param key  公钥
     * @param data 待加密数据
     * @return byte[]
     */
    public static byte[] encryptByPublicKey(String key, byte[] data) {
        byte[] result = null;
        try {
            // 获取公钥字符串时,进行了encryptBase64操作,因此此处需对公钥钥解密
            byte[] bytes = decryptBase64(key);
            // 取得公钥
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PublicKey publicKey = factory.generatePublic(keySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return blockEncrypt(cipher, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 公钥加密
     *
     * @param key key
     * @param dataStr str
     * @return str
     */
    public static String encryptByPublicKey(String key, String dataStr) {
        try {
            byte[] result = encryptByPublicKey(key, dataStr.getBytes("UTF-8"));
            return encryptBase64(result);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * 私钥解密
     *
     * @param data 加密数据
     * @param key  私钥
     * @return byte[]
     */
    public static byte[] decryptByPrivateKey(String key, byte[] data) {
        byte[] result = null;
        try {
            // 获取私钥字符串时,进行了encryptBase64操作,因此此处需对私钥解密
            byte[] bytes = decryptBase64(key);
            // 取得私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            // 对数据解密
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return blockDecrypt(cipher, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String decryptByPrivateKey(String key, String dataStr) {
        try {
            byte[] result = decryptByPrivateKey(key, decryptBase64(dataStr));
            return new String(result);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @param key  私钥
     * @return byte[]
     */
    public static byte[] encryptByPrivateKey(String key, byte[] data) {
        byte[] result = null;
        try {
            byte[] bytes = decryptBase64(key);
            // 取得私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return blockEncrypt(cipher, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对数据分块加密，防止太长超出加密长度
     *
     * @param cipher cipher
     * @param data data
     * @return byte[]
     */
    private static byte[] blockEncrypt(Cipher cipher, byte[] data) {
        int inputLen = data.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            return encryptedData;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

    }

    /**
     * 私钥加密
     *
     * @param key
     * @param dataStr
     * @return
     */
    public static String encryptByPrivateKey(String key, String dataStr) {
        try {
            byte[] result = encryptByPrivateKey(key, dataStr.getBytes("UTF-8"));
            return encryptBase64(result);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 公钥钥解密
     *
     * @param key  公钥
     * @param data 加密数据
     * @return
     */
    public static byte[] decryptByPublicKey(String key, byte[] data) {
        byte[] result = null;
        try {
            // 对公钥解密
            byte[] bytes = decryptBase64(key);
            // 取得公钥
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PublicKey publicKey = factory.generatePublic(keySpec);
            // 对数据解密
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return blockDecrypt(cipher, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 分块解密
     *
     * @param cipher
     * @param data
     * @return
     */
    private static byte[] blockDecrypt(Cipher cipher, byte[] data) {
        int inputLen = data.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher
                            .doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher
                            .doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            return decryptedData;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

    }


    public static String decryptByPublicKey(String key, String dataStr) {
        try {
            byte[] result = decryptByPublicKey(key, decryptBase64(dataStr));
            return new String(result);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 获取公钥
     *
     * @param map
     * @return
     */
    public static String getPublicKey(Map<String, Object> map) {
        String str = "";
        try {
            Key key = (Key) map.get(KEY_RSA_PUBLICKEY);
            str = encryptBase64(key.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return str;
    }

    /**
     * 获取私钥
     *
     * @param map
     * @return
     */
    public static String getPrivateKey(Map<String, Object> map) {
        String str = "";
        try {
            Key key = (Key) map.get(KEY_RSA_PRIVATEKEY);
            str = encryptBase64(key.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return str;
    }

    /**
     * BASE64 解密
     *
     * @param key 需要解密的字符串
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] decryptBase64(String key) {
        return javax.xml.bind.DatatypeConverter.parseBase64Binary(key);
    }

    /**
     * BASE64 加密
     *
     * @param key 需要加密的字节数组
     * @return 字符串
     * @throws Exception
     */
    public static String encryptBase64(byte[] key) {
        return javax.xml.bind.DatatypeConverter.printBase64Binary(key);
    }

}
