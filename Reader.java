import java.util.Scanner;

public class Reader {
    private Scanner scanner;

    public Reader(Scanner scanner) {
        this.scanner = scanner;
    }

    public void menu() {
        while (true) {
            System.out.println("=== 读者操作菜单 ===");
            System.out.println("1. 借阅书籍");
            System.out.println("2. 归还书籍");
            System.out.println("3. 查看所有书籍");
            System.out.println("4. 查看可借阅书籍");
            System.out.println("5. 返回主菜单");
            System.out.print("请选择操作: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    borrowBook();
                    break;
                case "2":
                    returnBook();
                    break;
                case "3":
                    viewAllBooks();
                    break;
                case "4":
                    viewAvailableBooks();
                    break;
                case "5":
                    System.out.println("返回主菜单...");
                    return;
                default:
                    System.out.println("无效选择，请重新输入");
            }
        }
    }

    // 借书
    private void borrowBook() {
        System.out.print("请输入要借阅的书籍ID: ");
        String bookId = scanner.nextLine();

        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("书籍不存在");
            return;
        }

        if (book.isBorrowed()) {
            System.out.println("该书已被借出");
            return;
        }

        book.setBorrowed(true);
        System.out.println("借阅成功！书名：" + book.getName());
        System.out.println("请通过菜单写入书籍文件");
    }

    // 还书
    private void returnBook() {
        System.out.print("请输入要归还的书籍ID: ");
        String bookId = scanner.nextLine();

        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("书籍不存在");
            return;
        }

        if (!book.isBorrowed()) {
            System.out.println("该书未被借出，无需归还");
            return;
        }

        book.setBorrowed(false);
        System.out.println("归还成功！感谢使用");
        System.out.println("请通过菜单写入书籍文件");
    }

    // 查看所有书
    private void viewAllBooks() {
        if (LibrarySystem.DataStore.books.isEmpty()) {
            System.out.println("图书馆暂无书籍");
            return;
        }

        System.out.println("\n=== 所有书籍列表 ===");
        for (Book book : LibrarySystem.DataStore.books) {
            String status = book.isBorrowed() ? "已借出" : "可借阅";
            System.out.println("ID: " + book.getId() + " | 书名: " + book.getName() + " | 状态: " + status);
        }
    }

    // 查看可借阅书
    private void viewAvailableBooks() {
        boolean hasAvailable = false;
        System.out.println("\n=== 可借阅书籍 ===");
        for (Book book : LibrarySystem.DataStore.books) {
            if (!book.isBorrowed()) {
                hasAvailable = true;
                System.out.println("ID: " + book.getId() + " | 书名: " + book.getName());
            }
        }
        if (!hasAvailable) {
            System.out.println("暂无可借阅书籍");
        }
    }

    // ID查找书籍
    private Book findBookById(String bookId) {
        for (Book book : LibrarySystem.DataStore.books) {
            if (book.getId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }
}
