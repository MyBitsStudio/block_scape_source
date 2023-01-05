package com.rs.cores;

import sun.security.util.SecurityConstants;

import java.security.Permission;

public class SecurityHandler {

    public static SecurityHandler handler;

    private static final ThreadGroup rootGroup = getThreadGroup();

    public static SecurityHandler getHandler() {
          if (handler == null)
             handler = new SecurityHandler();
          return handler;
    }

    public SecurityHandler(){
       handler = this;
    }

    public static ThreadGroup getThreadGroup() {
        ThreadGroup root = Thread.currentThread().getThreadGroup();
        ThreadGroup parent;
        while ((parent = root.getParent()) != null)
            root = parent;
        return root;
    }

    public void checkAccess(Thread t) {
        if (t == null) {
            throw new NullPointerException("thread can't be null");
        }
        if (t.getThreadGroup() == rootGroup) {
            checkPermission(SecurityConstants.MODIFY_THREAD_PERMISSION);
        }  // just return

    }

    public void checkPermission(Permission perm) {
        if (perm == null) {
            throw new NullPointerException("permission can't be null");
        }
    }

}
