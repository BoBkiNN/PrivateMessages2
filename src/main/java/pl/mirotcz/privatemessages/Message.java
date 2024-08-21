package pl.mirotcz.privatemessages;

import pl.mirotcz.privatemessages.spigot.utils.StringUtils;

public class Message {
    private String sender_name;
    private String recipient_name;
    private long date;
    private String message_content;

    public Message(String sender, String recipient, long date, String content) {
        this.sender_name = sender;
        this.recipient_name = recipient;
        this.date = date;
        this.message_content = content;
    }

    public String getSenderName() {
        return this.sender_name;
    }

    public synchronized void setSenderName(String name) {
        this.sender_name = name;
    }

    public String getRecipientName() {
        return this.recipient_name;
    }

    public synchronized void setRecipientName(String name) {
        this.recipient_name = name;
    }

    public long getDate() {
        return this.date;
    }

    public synchronized void setDate(long date) {
        this.date = date;
    }

    public String getMessageContent() {
        return this.message_content;
    }

    public synchronized void setMessageContent(String content) {
        this.message_content = content;
    }

    public String toString() {
        return this.getSenderName() + ";" +
                this.getRecipientName() + ";" +
                this.getDate() + ";" +
                StringUtils.getSafeString(this.getMessageContent());
    }
}
