// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.service;

import com.nomade.domain.Account;
import com.nomade.service.AccountService;
import java.math.BigInteger;
import java.util.List;

privileged aspect AccountService_Roo_Service {
    
    public abstract long AccountService.countAllAccounts();    
    public abstract void AccountService.deleteAccount(Account account);    
    public abstract Account AccountService.findAccount(BigInteger id);    
    public abstract List<Account> AccountService.findAllAccounts();    
    public abstract List<Account> AccountService.findAccountEntries(int firstResult, int maxResults);    
    public abstract void AccountService.saveAccount(Account account);    
    public abstract Account AccountService.updateAccount(Account account);    
}
