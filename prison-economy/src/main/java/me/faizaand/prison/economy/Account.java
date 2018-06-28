package me.faizaand.prison.economy;

import me.faizaand.prison.store.Document;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {

    private UUID uid;
    private BigDecimal amount;

    public Account(UUID uid, BigDecimal amount) {
        this.uid = uid;
        this.amount = amount;
    }

    public Account(Document doc) {
        this.uid = UUID.fromString((String) doc.get("uuid"));
        this.amount = new BigDecimal((String) doc.get("amount"));
    }

    public Document toDocument() {
        Document doc = new Document();
        doc.put("uid", uid.toString());
        doc.put("amount", amount.toString());
        return doc;
    }

    public void give(BigDecimal value) {
        this.amount = this.amount.add(value);
    }

    public void take(BigDecimal value) {
        this.amount = this.amount.subtract(value);
    }

    public void set(BigDecimal value) {
        this.amount = value;
    }

    public UUID getUid() {
        return uid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
