import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ChatMessage {
    private String sender;
    private String time;
    private String content;

    public ChatMessage(String sender, String time, String content) {
        this.sender = sender;
        this.time = time;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }
}

class JurnalHarian {
    private String title;
    private String time;
    private String content;

    public JurnalHarian(String title, String time, String content) {
        this.title = title;
        this.time = time;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }
}

class User {
    public String username;
    private String password;
    private List<JurnalHarian> jurnalHarians;
    private List<ChatMessage> chatRoom; // Chat room untuk pengguna

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.jurnalHarians = new ArrayList<>();
        this.chatRoom = new ArrayList<>();
    }

    public void nulisJurnal(String title, String time, String content) {
        JurnalHarian jurnalHarian = new JurnalHarian(title, time, content);
        jurnalHarians.add(jurnalHarian);
    }

    public List<JurnalHarian> getJurnalHarians() {
        return jurnalHarians;
    }

    public void sendMessageToChatRoom(String time, String content, AnonymousStoryTeller anonymousStoryTeller) {
        ChatMessage message = new ChatMessage(username, time, content);
        chatRoom.add(message);
        anonymousStoryTeller.postChatMessage(message);
    }

    public List<ChatMessage> getChatRoom() {
        return chatRoom;
    }

    public boolean signIn(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void signOut() {
    }

    public void postAnonymousStory(String title, String time, String content, AnonymousStoryTeller anonymousStoryTeller) {
        JurnalHarian jurnalHarian = new JurnalHarian(title, time, content);
        anonymousStoryTeller.postAnonymousStory(jurnalHarian);
    }
}

class AnonymousStoryTeller {
    private List<JurnalHarian> anonymousStories;
    private List<ChatMessage> chatMessages; // Chat room anonim

    public AnonymousStoryTeller() {
        this.anonymousStories = new ArrayList<>();
        this.chatMessages = new ArrayList<>();
    }

    public void postAnonymousStory(JurnalHarian jurnalHarian) {
        anonymousStories.add(jurnalHarian);
    }

    public void postChatMessage(ChatMessage message) {
        chatMessages.add(message);
    }

    public List<JurnalHarian> getAnonymousStories() {
        return anonymousStories;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }
}

class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }

    public void deleteUser(String username, List<User> users) {
        for (User user : users) {
            if (user.username.equals(username)) {
                users.remove(user);
                System.out.println("Pengguna " + username + " telah dihapus.");
                return;
            }
        }
        System.out.println("Pengguna " + username + " tidak ditemukan.");
    }

    public void deleteAnonymousStory(JurnalHarian jurnalHarian, AnonymousStoryTeller anonymousStoryTeller) {
        if (anonymousStoryTeller.getAnonymousStories().remove(jurnalHarian)) {
            System.out.println("Cerita anonim telah dihapus.");
        } else {
            System.out.println("Cerita anonim tidak ditemukan.");
        }
    }

    public List<JurnalHarian> getAllStories(AnonymousStoryTeller anonymousStoryTeller) {
        return anonymousStoryTeller.getAnonymousStories();
    }

    public void viewChatMessages(AnonymousStoryTeller anonymousStoryTeller) {
        List<ChatMessage> chatMessages = anonymousStoryTeller.getChatMessages();
        for (ChatMessage message : chatMessages) {
            System.out.println("Pengirim: " + message.getSender());
            System.out.println("Waktu: " + message.getTime());
            System.out.println("Pesan: " + message.getContent());
            System.out.println();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        int pilihan = -1;
        Admin admin = null;
        User user = null;
        AnonymousStoryTeller anonymousStoryTeller = new AnonymousStoryTeller();
        Scanner scanner = new Scanner(System.in);

        while (pilihan != 3) {
            System.out.println("1. Masuk sebagai admin");
            System.out.println("2. Masuk sebagai pengguna");
            System.out.println("3. Keluar");
            System.out.print("Pilih: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();

            if (pilihan == 1) {
                String usernameAdmin, passwordAdmin;

                System.out.print("Username admin: ");
                usernameAdmin = scanner.nextLine();

                System.out.print("Password admin: ");
                passwordAdmin = scanner.nextLine();

                admin = new Admin(usernameAdmin, passwordAdmin);

                if (admin.signIn(usernameAdmin, passwordAdmin)) {
                    System.out.println("Admin masuk.");
                    int pilihanAdmin = -1;
                    while (pilihanAdmin != 5) {
                        System.out.println("1. Lihat semua cerita anonim");
                        System.out.println("2. Hapus cerita anonim");
                        System.out.println("3. Hapus pengguna");
                        System.out.println("4. Lihat pesan chat anonim");
                        System.out.println("5. Keluar");
                        System.out.print("Pilih: ");
                        pilihanAdmin = scanner.nextInt();
                        scanner.nextLine();

                        if (pilihanAdmin == 1) {
                            List<JurnalHarian> semuaCerita = admin.getAllStories(anonymousStoryTeller);
                            for (JurnalHarian cerita : semuaCerita) {
                                System.out.println("Judul: " + cerita.getTitle());
                                System.out.println("Waktu: " + cerita.getTime());
                                System.out.println("Isi: " + cerita.getContent());
                                System.out.println();
                            }
                        } else if (pilihanAdmin == 2) {
                            System.out.print("Masukkan judul cerita anonim yang ingin dihapus: ");
                            String judul = scanner.nextLine();
                            List<JurnalHarian> anonymousStories = admin.getAllStories(anonymousStoryTeller);
                            JurnalHarian toDelete = null;
                            for (JurnalHarian cerita : anonymousStories) {
                                if (cerita.getTitle().equals(judul)) {
                                    toDelete = cerita;
                                    break;
                                }
                            }
                            if (toDelete != null) {
                                admin.deleteAnonymousStory(toDelete, anonymousStoryTeller);
                            } else {
                                System.out.println("Cerita anonim tidak ditemukan.");
                            }
                        } else if (pilihanAdmin == 3) {
                            System.out.print("Masukkan username pengguna yang ingin dihapus: ");
                            String usernameToDelete = scanner.nextLine();
                            admin.deleteUser(usernameToDelete, users);
                        } else if (pilihanAdmin == 4) {
                            admin.viewChatMessages(anonymousStoryTeller);
                        }
                    }
                } else {
                    System.out.println("Admin tidak valid.");
                }
            } else if (pilihan == 2) {
                String usernameUser, passwordUser;

                System.out.print("Username pengguna: ");
                usernameUser = scanner.nextLine();

                System.out.print("Password pengguna: ");
                passwordUser = scanner.nextLine();

                user = new User(usernameUser, passwordUser);
                users.add(user); // Tambahkan pengguna ke daftar pengguna.

                if (user.signIn(usernameUser, passwordUser)) {
                    System.out.println("Pengguna masuk.");
                    int pilihanUser = -1;
                    while (pilihanUser != 6) {
                        System.out.println("1. Tulis jurnal");
                        System.out.println("2. Lihat jurnal");
                        System.out.println("3. Hapus akun");
                        System.out.println("4. Posting cerita anonim");
                        System.out.println("5. Lihat pesan chat anonim");
                        System.out.println("6. Keluar");
                        System.out.print("Pilih: ");
                        pilihanUser = scanner.nextInt();
                        scanner.nextLine();

                        if (pilihanUser == 1) {
                            System.out.print("Judul: ");
                            String judul = scanner.nextLine();
                            System.out.print("Waktu: ");
                            String waktu = scanner.nextLine();
                            System.out.print("Isi: ");
                            String isi = scanner.nextLine();
                            user.nulisJurnal(judul, waktu, isi);
                            System.out.println("Jurnal berhasil ditulis.");
                        } else if (pilihanUser == 2) {
                            List<JurnalHarian> jurnals = user.getJurnalHarians();
                            for (JurnalHarian jurnal : jurnals) {
                                System.out.println("Judul: " + jurnal.getTitle());
                                System.out.println("Waktu: " + jurnal.getTime());
                                System.out.println("Isi: " + jurnal.getContent());
                                System.out.println();
                            }
                        } else if (pilihanUser == 3) {
                            // Hapus akun pengguna
                            users.remove(user);
                            System.out.println("Akun pengguna dihapus.");
                            break;
                        } else if (pilihanUser == 4) {
                            System.out.print("Judul cerita anonim: ");
                            String judul = scanner.nextLine();
                            System.out.print("Waktu cerita anonim: ");
                            String waktu = scanner.nextLine();
                            System.out.print("Isi cerita anonim: ");
                            String isi = scanner.nextLine();
                            user.postAnonymousStory(judul, waktu, isi, anonymousStoryTeller);
                            System.out.println("Cerita anonim berhasil diposting.");
                        } else if (pilihanUser == 5) {
                            admin.viewChatMessages(anonymousStoryTeller);
                        } else if (pilihanUser == 6) {
                            user.signOut();
                        }
                    }
                } else {
                    System.out.println("Pengguna tidak valid.");
                }
            }
        }
        scanner.close();
    }
}
