import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;

public class LibrarySystem
{
    public static class DataStore
    {
        public static final String BOOKS_FILE ="books.txt";
        public static final String USERS_FILE = "users.txt";
        static List<Book> books = new ArrayList<>();
        static public List<User> user = new ArrayList<>();
        static List<User> users = new ArrayList<>();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== 初始化数据：读取文件 ===");
        readFromFile(DataStore.BOOKS_FILE);
        readFromFile(DataStore.USERS_FILE);
        mainMenu(scanner);
        scanner.close();
        scanner.close();
    }

    private static void mainMenu(Scanner scanner) {
        while (true) {
            System.out.println("欢迎使用");
            System.out.println("=== 图书馆借阅系统 ===");
            System.out.println("1. 注册账号");
            System.out.println("2. 登录系统");
            System.out.println("3. 写入书籍文件");
            System.out.println("4. 写入用户文件");
            System.out.println("5. 读取书籍文件");
            System.out.println("6. 读取用户文件");
            System.out.println("7. 退出系统");
            System.out.print("请选择操作: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    handleRegistration(scanner);
                    break;
                case "2":
                    handleLogin(scanner);
                    break;
                case "3":
                    writeToFile(DataStore.BOOKS_FILE);
                    break;
                case "4":
                    writeToFile(DataStore.USERS_FILE);
                    break;
                case "5":
                    readFromFile(DataStore.BOOKS_FILE);
                    break;
                case "6":
                    readFromFile(DataStore.USERS_FILE);
                    break;
                case "7":
                    System.out.println("感谢使用，再见！");
                    return;
                default:
                    System.out.println("无效选择，请重新输入");
            }
        }
    }

    // 写入文件
    public static void writeToFile(String fileName) {
        Scanner scanner = new Scanner(System.in);
        FileWriter writer = null;

        try {
            writer = new FileWriter(fileName, true);
            System.out.println("请输入要写入文件的内容（输入exit结束）：");
            String line;

            while (true)
            {
                line = scanner.nextLine();
                if ("exit".equalsIgnoreCase(line))
                {
                    break;
                }
            }

            System.out.println("数据已成功写入文件：" + fileName);

        }
        catch (IOException e)
        {
            System.out.println("写入文件时出错：" + e.getMessage());
        }
        finally
        {
            try
            {
                if (writer != null)
                {
                    writer.close();
                }
                if (scanner != null)
                {
                    scanner.close();
                }
            }
            catch (IOException e)
            {
                System.out.println("关闭资源时出错：" + e.getMessage());
            }
        }
    }

    // 读取文件
    public static void readFromFile(String fileName)
    {
        Scanner fileScanner = null;
        try {
            File file = new File(fileName);

            if (file.exists())
            {
                fileScanner = new Scanner(file);
                System.out.println("文件内容如下：");
                int lineNumber = 1;

                while (fileScanner.hasNextLine())
                {
                    String line = fileScanner.nextLine();
                    System.out.println(lineNumber + ". " + line);
                    lineNumber++;
                }
            }
            else
            {
                System.out.println("文件不存在：" + fileName);
            }
            String line = "necessary";
            String line1 = "necessary";
            String line2 = "necessary";


        }
        catch (IOException e)
        {
            System.out.println("读取文件时出错：" + e.getMessage());
        }
        finally
        {
            if (fileScanner != null)
            {
                fileScanner.close();
            }
        }
    }

    // 注册,用User对象
    private static void handleRegistration(Scanner scanner) {
        System.out.println("=== 注册身份选择 ===");
        System.out.println("1. 读者");
        System.out.println("2. 管理员");
        System.out.print("请选择: ");
        String role = scanner.nextLine();
        boolean isAdmin = "2".equals(role);

        if (!"1".equals(role) && !"2".equals(role)) {
            System.out.println("无效选择，返回主菜单");
            return;
        }

        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        for (User user : DataStore.users)
        {
            if (user.getUsername().equals(username))
            {
                System.out.println("用户名已存在，注册失败");
                return;
            }
        }

        System.out.print("请输入密码: ");
        String password = scanner.nextLine();
        System.out.print("请确认密码: ");
        String confirm = scanner.nextLine();

        if (!password.equals(confirm))
        {
            System.out.println("两次密码不一致，注册失败");
            return;
        }

        DataStore.users.add(new User(username, password, isAdmin));
        System.out.println((isAdmin ? "管理员" : "读者") + "注册成功！");
        System.out.println("请通过菜单选择用户文件写入数据");
    }

    // 登录,用User对象
    private static void handleLogin(Scanner scanner)
    {
        System.out.println("=== 登录身份选择 ===");
        System.out.println("1. 读者");
        System.out.println("2. 管理员");
        System.out.print("请选择: ");
        String role = scanner.nextLine();
        boolean isAdmin = "2".equals(role);

        if (!"1".equals(role) && !"2".equals(role))
        {
            System.out.println("无效选择，返回主菜单");
            return;
        }

        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();

        User loginUser = null;
        for (User user : DataStore.users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password) && user.isAdmin() == isAdmin)
            {
                loginUser = user;
                break;
            }
        }

        if (loginUser != null)
        {
            System.out.println("登录成功！欢迎回来，" + username + "！");
            if (isAdmin)
            {
                new Admin(scanner).menu();
            }
            else
            {
                new Reader(scanner).menu();
            }
        }
        else
        {
            System.out.println("用户名或密码错误");
        }
    }
}