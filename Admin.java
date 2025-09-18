import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
public class Admin {
    private Scanner scanner;

    public Admin(Scanner scanner) {
        this.scanner = scanner;
    }

    public void menu() {
        while (true) {
            System.out.println("=== 管理员操作菜单 ===");
            System.out.println("1. 添加书籍（含数量）");
            System.out.println("2. 删除书籍");
            System.out.println("3. 查看所有书籍");
            System.out.println("4. 查看借出书籍");
            System.out.println("5. 注册新管理员");
            System.out.println("6. 返回主菜单");
            System.out.print("请选择操作: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addBookWithQuantity();
                    break;
                case "2":
                    deleteBook();
                    break;
                case "3":
                    viewAllBooks();
                    break;
                case "4":
                    viewBorrowedBooks();
                    break;
                case "5":
                    registerNewAdmin();
                    break;
                case "6":
                    System.out.println("返回主菜单...");
                    return;
                default:
                    System.out.println("无效选择，请重新输入");
            }
        }
    }

    // 新增书
    private void addBookWithQuantity() {
        System.out.println("\n=== 添加书籍（含数量） ===");
        System.out.print("请输入书籍ID: ");
        String bookId = scanner.nextLine().trim();

        if (findBookById(bookId) != null) {
            System.out.println("该ID已存在，添加失败");
            return;
        }

        System.out.print("请输入书名: ");
        String bookName = scanner.nextLine().trim();

        int quantity = 0;
        while (true)
        {
            System.out.print("请输入书籍数量: ");
            String quantityStr = scanner.nextLine().trim();
            try
            {
                quantity = Integer.parseInt(quantityStr);
                if (quantity > 0)
                {
                    break;
                }
                else
                {
                    System.out.println("数量必须为正整数，请重新输入");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("输入格式错误，请输入数字");
            }
        }

        Book newBook = new Book(bookId, bookName, quantity);
        LibrarySystem.DataStore.books.add(newBook);
        System.out.println("添加成功！书籍信息：");
        System.out.println("ID: " + bookId + " | 书名: " + bookName + " | 数量: " + quantity);

        writeBookToFile(bookId, bookName, quantity);
    }

    // 新增书，输入文件
    private void writeBookToFile(String id, String name, int quantity)
    {
        String fileName = LibrarySystem.DataStore.BOOKS_FILE;
        try (FileWriter writer = new FileWriter(fileName, true))
        {
            String content = id + "," + name + "," + quantity + " ";
            writer.write(content);
            System.out.println("书籍信息已成功写入文件：" + fileName);
        }
        catch (IOException e)
        {
            System.out.println("写入文件失败：" + e.getMessage());
        }
    }

    // 删除书
    private void deleteBook() {
        System.out.print("请输入要删除的书籍ID: ");
        String bookId = scanner.nextLine();

        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("书籍不存在");
            return;
        }

        if (book.isBorrowed()) {
            System.out.println("该书已被借出，无法删除");
            return;
        }

        LibrarySystem.DataStore.books.remove(book);
        System.out.println("删除成功！已移除书籍：" + book.getName());
        System.out.println("请手动更新书籍文件或使用批量写入功能");
    }

    // 查看所有书籍
    private void viewAllBooks() {
        if (LibrarySystem.DataStore.books.isEmpty()) {
            System.out.println("图书馆暂无书籍");
            return;
        }

        System.out.println("=== 所有书籍列表（含数量） ===");
        for (Book book : LibrarySystem.DataStore.books) {
            String status = book.isBorrowed() ? "已借出" : "可借阅";
            System.out.printf("ID: %s | 书名: %s | 数量: %d | 状态: %s%n",
                    book.getId(), book.getName(), book.getQuantity(), status);
        }
    }

    // 查看借出书籍
    private void viewBorrowedBooks() {
        boolean hasBorrowed = false;
        System.out.println("=== 已借出书籍 ===");
        for (Book book : LibrarySystem.DataStore.books) {
            if (book.isBorrowed()) {
                hasBorrowed = true;
                System.out.println("ID: " + book.getId() + " | 书名: " + book.getName());
            }
        }
        if (!hasBorrowed) {
            System.out.println("暂无借出书籍");
        }
    }

    //注册新管理员
    private void registerNewAdmin() {
        System.out.print("请输入新管理员用户名: ");
        String username = scanner.nextLine().trim();
        for (User user : LibrarySystem.DataStore.users) {
            if (user.getUsername().equals(username)) {
                System.out.println("用户名已存在");
                return;
            }
        }

        System.out.print("请输入密码: ");
        String password = scanner.nextLine().trim();
        System.out.print("请确认密码: ");
        String confirm = scanner.nextLine().trim();

        if (!password.equals(confirm)) {
            System.out.println("两次密码不一致，注册失败");
            return;
        }

        LibrarySystem.DataStore.users.add(new User(username, password, true));
        System.out.println("新管理员注册成功！请通过菜单写入用户文件");
    }

    //ID查找书籍
    private Book findBookById(String bookId) {
        for (Book book : LibrarySystem.DataStore.books) {
            if (book.getId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }
}