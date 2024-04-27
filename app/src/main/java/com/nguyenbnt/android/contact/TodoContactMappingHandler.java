package com.nguyenbnt.android.contact;

import com.nguyenbnt.android.contact.ContactMappingHandler;

public class TodoContactMappingHandler implements ContactMappingHandler {
    @Override
    public String getContactName(String phoneNumber) {
        return "TODO";
    }
}
