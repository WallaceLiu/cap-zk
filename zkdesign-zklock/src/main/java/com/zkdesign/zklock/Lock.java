package com.zkdesign.zklock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lock {
    private String lockId;
    private String path;
    private boolean active;

    public Lock(String lockId, String path) {
        this.lockId = lockId;
        this.path = path;
    }
}