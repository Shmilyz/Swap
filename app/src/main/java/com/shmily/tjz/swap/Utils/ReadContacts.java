package com.shmily.tjz.swap.Utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.shmily.tjz.swap.Db.Contacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shmily_Z on 2017/6/8.
 */

public class ReadContacts {
    private List<Contacts> resultlist =new ArrayList<>();
    private Cursor cursor = null;

    public List<Contacts> getContacts(){


        try {
            // 查询联系人数据
            cursor = MyApplication.getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {

                        // 获取联系人姓名
                        String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        // 获取联系人手机号
                        String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Contacts contacts = new Contacts();
                        contacts.setName(displayName);
                        contacts.setNumber(number);
                        resultlist.add(contacts);

                    }
                }
                else{
                    Contacts contacts = new Contacts();
                    contacts.setName("暂时无数据");
                    contacts.setNumber("暂时无数据");
                    resultlist.add(contacts);

                }
            }

        } catch (Exception e) {


            e.printStackTrace();

        }


        finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        return resultlist;
    }
}
