package org.example;

import java.awt.Desktop;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LinkManager {
    private Map<String, Link> links = new HashMap<>();
    private UserManager userManager;

    public LinkManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public String shortenLink(String longUrl, int limit, int durationInSeconds) {
        String userId = userManager.getCurrentUser();
        long timestamp = System.currentTimeMillis(); // Уникальный идентификатор
        String shortUrl = "short.ly/" + longUrl.hashCode() + "_" + userId.hashCode() + "_" + timestamp;

        long expirationTime = System.currentTimeMillis() + (durationInSeconds * 1000L);
        links.put(shortUrl, new Link(longUrl, limit, userId, expirationTime));
        return shortUrl;
    }

    public void goToLink(String shortUrl) {
        cleanupExpiredLinks(); // Удаляем устаревшие ссылки

        Link link = links.get(shortUrl);

        if (link == null) {
            System.out.println("Ссылка не найдена.");
            return;
        }

        if (!link.getUserId().equals(userManager.getCurrentUser())) {
            System.out.println("У вас нет доступа к этой ссылке.");
            return;
        }

        if (link.getLimit() <= 0) {
            System.out.println("Лимит переходов по ссылке исчерпан.");
            return;
        }

        if (System.currentTimeMillis() > link.getExpirationTime()) {
            System.out.println("Срок действия ссылки истек.");
            links.remove(shortUrl); // Удаляем ссылку
            return;
        }

        try {
            Desktop.getDesktop().browse(new URI(link.getLongUrl()));
            link.decreaseLimit();
            System.out.println("Переход выполнен. Осталось переходов: " + link.getLimit());
        } catch (Exception e) {
            System.out.println("Ошибка при переходе: " + e.getMessage());
        }
    }

    public void viewLinks() {
        cleanupExpiredLinks(); // Удаляем устаревшие ссылки

        String currentUser = userManager.getCurrentUser();
        links.entrySet().stream()
                .filter(entry -> entry.getValue().getUserId().equals(currentUser))
                .forEach(entry -> {
                    Link link = entry.getValue();
                    System.out.println("Короткая: " + entry.getKey() + " -> Длинная: " + link.getLongUrl() +
                            " (Осталось переходов: " + link.getLimit() + ", Срок действия: " +
                            (link.getExpirationTime() - System.currentTimeMillis()) / 1000 + " секунд)");
                });
    }

    private void cleanupExpiredLinks() {
        Iterator<Map.Entry<String, Link>> iterator = links.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Link> entry = iterator.next();
            if (System.currentTimeMillis() > entry.getValue().getExpirationTime()) {
                iterator.remove();
            }
        }
    }
}

class Link {
    private String longUrl;
    private int limit;
    private String userId;
    private long expirationTime;

    public Link(String longUrl, int limit, String userId, long expirationTime) {
        this.longUrl = longUrl;
        this.limit = limit;
        this.userId = userId;
        this.expirationTime = expirationTime;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public int getLimit() {
        return limit;
    }

    public String getUserId() {
        return userId;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void decreaseLimit() {
        this.limit--;
    }
}
