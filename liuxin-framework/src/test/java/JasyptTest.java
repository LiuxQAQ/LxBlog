import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.StandardEnvironment;

public class JasyptTest {
    @Test
    public void test(){
        // 对应配置文件中配置的加密密钥
        System.setProperty("jasypt.encryptor.password", "liuxin");
        StringEncryptor stringEncryptor = new DefaultLazyEncryptor(new StandardEnvironment());
        System.out.println("加密： " + stringEncryptor.encrypt("liuxin200115"));
        System.out.println("解密： " + stringEncryptor.decrypt("N/+f2B9SznK4MUDSp24Upw=="));
    }
}
