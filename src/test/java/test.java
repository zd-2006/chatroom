
import com.chatroom.Service.UserService;
import com.chatroom.mapper.UserMapper;
import com.chatroom.pojo.User;
import com.chatroom.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Scanner;

public class test {
    @Test
    public void test1() {
        // 1. 获取数据库连接
        System.out.println("正在连接数据库...");
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            // 2. 获取 Mapper 接口代理
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            UserService userservice = new UserService();
            // 3. 测试插入（注册）
            String name=null;
            String password=null;
            String rePassword=null;
            Scanner sc = new Scanner(System.in);
            System.out.println("--- 测试注册 ---");
            System.out.println("请输入你的昵称");
            name = sc.nextLine();
            boolean isSame = false;
            if(isSame=setpassword(name,password)){
                boolean isRegister = userservice.register(name,password);
                if(isRegister){
                    System.out.println("昵称已存在,请重新输入");
                }
            }else {
                System.out.println("两次输入不一样,重修输入");
                setpassword(name,password);
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean setpassword(String password, String rePassword) {
        Scanner sc = new Scanner(System.in);
        password = sc.nextLine();
        rePassword = sc.nextLine();
        if (password.equals(rePassword)) {
            return true;
        }else
            return false;
    }
}