package me.faizaand.prison.economy;

import me.faizaand.prison.output.Output;
import me.faizaand.prison.store.Collection;
import me.faizaand.prison.store.Document;

import java.math.BigDecimal;
import java.util.*;

public class EconomyManager {

    private Collection accountsColl;
    private Map<UUID, Account> accounts;

    public EconomyManager() {
        this.accounts = new HashMap<>();
    }

    public boolean load() {
        this.accountsColl = PrisonEconomy.getInstance().getDatabase().getCollection("accounts").orElseGet(
                () -> PrisonEconomy.getInstance().getDatabase().createCollection("accounts")
        );
        if (this.accountsColl == null) {
            Output.get().logError("could not get or create accounts collection in the economy database, check file permissions");
            PrisonEconomy.getInstance().getStatus().toFailed("could not create accounts collection, see console");
            return false;
        }

        List<Document> accountDocs = this.accountsColl.getAll();
        for (Document accountDoc : accountDocs) {
            Account a = new Account(accountDoc);
            this.accounts.put(a.getUid(), a);
        }

        return true;
    }

    public void save() {
        this.accounts.forEach((uuid, account) -> {
            this.accountsColl.insert(String.valueOf(uuid.getMostSignificantBits()), account.toDocument());
        });
    }

    public Account createAccount(UUID forPlayer) {
        Account a = new Account(forPlayer, new BigDecimal("0"));
        this.accounts.put(forPlayer, a);
        return a;
    }

    public Optional<Account> getAccount(UUID forPlayer) {
        return Optional.ofNullable(this.accounts.get(forPlayer));
    }

    public Map<UUID, Account> getAccounts() {
        return accounts;
    }

}
