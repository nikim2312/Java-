package org.example;

import java.util.Scanner;

public class Main {
    private static UserManager userManager = new UserManager();
    private static LinkManager linkManager = new LinkManager(userManager);  // Передаем userManager в LinkManager
    private static boolean isAuthenticated = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (isAuthenticated) {
                System.out.println("Добро пожаловать! Что вы хотите сделать?");
                System.out.println("1. Сократить ссылку");
                System.out.println("2. Перейти по короткой ссылке");
                System.out.println("3. Просмотр ваших ссылок");
                System.out.println("4. Выйти");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        shortenLink(scanner);
                        break;
                    case "2":
                        goToLink(scanner);
                        break;
                    case "3":
                        viewLinks();
                        break;
                    case "4":
                        System.out.println("Выход...");
                        isAuthenticated = false;
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                }
            } else {
                System.out.println("1. Регистрация");
                System.out.println("2. Войти по UUID");
                System.out.println("3. Выйти");
                String action = scanner.nextLine();
                switch (action) {
                    case "1":
                        registerUser(scanner);
                        break;
                    case "2":
                        loginUser(scanner);
                        break;
                    case "3":
                        System.out.println("Выход...");
                        return;
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.println("Введите ваш ник:");
        String username = scanner.nextLine();
        String uuid = userManager.registerUser(username);
        System.out.println("Вы успешно зарегистрированы! Ваш UUID: " + uuid);
    }

    private static void loginUser(Scanner scanner) {
        System.out.println("Введите UUID для авторизации:");
        String uuid = scanner.nextLine();
        if (userManager.loginUser(uuid)) {
            isAuthenticated = true;
            System.out.println("Авторизация успешна! Теперь вы можете пользоваться функционалом.");
        } else {
            System.out.println("Неверный UUID. Попробуйте снова.");
        }
    }

    private static void shortenLink(Scanner scanner) {
        System.out.println("Введите длинную ссылку:");
        String longUrl = scanner.nextLine();
        System.out.println("Введите лимит переходов по ссылке:");
        int limit = Integer.parseInt(scanner.nextLine());
        System.out.println("Введите срок действия ссылки в секундах:");
        int durationInSeconds = Integer.parseInt(scanner.nextLine());

        // Теперь передаем все три параметра
        String shortUrl = linkManager.shortenLink(longUrl, limit, durationInSeconds);
        System.out.println("Ваша короткая ссылка: " + shortUrl);
    }

    private static void goToLink(Scanner scanner) {
        System.out.println("Введите короткую ссылку:");
        String shortUrl = scanner.nextLine();
        linkManager.goToLink(shortUrl);
    }

    private static void viewLinks() {
        System.out.println("Список ваших ссылок:");
        linkManager.viewLinks();
    }
}
