package com.solaris.android_third_party.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.solaris.android_third_party.BaseActivity;
import com.solaris.android_third_party.R;
import com.solaris.android_third_party.model.User;
import com.solaris.android_third_party.utils.RandomUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by hanxirui on 2016/12/24.
 */

public class RealmActivity extends BaseActivity {

    private byte[] key = "keykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeyy".getBytes();

    private String account = "account";

    private RealmConfiguration realmConfiguration;

    private Realm realm;

    private TextView show;

    private StringBuffer result;

    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm);

        show = (TextView)findViewById(R.id.show);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        result = new StringBuffer();

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                testRealm();
            }
        });

//        2.configuration
        realmConfiguration = new RealmConfiguration
                .Builder(this)
                .name(account)//如果你想設置個別資料庫名稱
                .encryptionKey(key)//如果你需要加密的話
                .build();

//        3.getInstance
//default
//        realm = Realm.getInstance(Context);

//use configuration
        realm = Realm.getInstance(realmConfiguration);


    }


    public void testRealm() {
//        為了Thread safety，每當從Realm取得的資料要修改刪除時，必須...
//        realm.beginTransaction();
////... 寫這裡 ...
//        realm.commitTransaction();
//        也許你想修改資料，但此時並不想commit進去，你可以...
//        realm.beginWriteTransaction();
////... 寫這裡 ...
//        realm.cancelTransaction();
//        或是...
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                User user = realm.createObject(User.class);
//                user.setName("Cuber");
//                user.setAge(26);
//            }
//        });
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.createObject(User.class);
                user.setName("Cuber00000");
                user.setAge(26);

            }
        });
//        存入一個RealmObject
        realm.beginTransaction();
        User person = new User();
        person.setName("Cuber" + RandomUtil.generateRandomString(5));
        person.setAge(26);
        realm.copyToRealm(person);
        realm.commitTransaction();

//        存入一個RealmList
        List<User> friendList = new ArrayList<User>();
        User person1 = new User();
        person1.setName("Pie" + RandomUtil.generateRandomString(5));
        person1.setAge(RandomUtil.randomBetween(1, 100));
        friendList.add(person1);
        User person2 = new User();
        person2.setName("Peter" + RandomUtil.generateRandomString(5));
        person2.setAge(RandomUtil.randomBetween(1, 100));
        friendList.add(person2);
        User person3 = new User();
        person3.setName("Susan" + RandomUtil.generateRandomString(5));
        person3.setAge(RandomUtil.randomBetween(1, 100));
        friendList.add(person3);

        realm.beginTransaction();
        realm.copyToRealm(friendList);
        realm.commitTransaction();

//        如果你有設@PrimaryKey, 就可以針對key做update
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(friendList);
//        realm.commitTransaction();
//        注意
//        1.必須在你getInstance的那條Thread做Write
//        2.Realm存入的都是以RealmObject為單位，所以如果寫入了person, friendList後
//        當你下query Person.class的時候這會把所有的Person都會被抓出
//        7.Queries
//
//                先where到你要的class
        RealmQuery<User> query = realm.where(User.class);
//        在下條件給他
        RealmResults<User> result1 = query.beginsWith("name", "Cuber").findAll();

        result.append("Cuber====" + result1.size()+"\r\n");
        Iterator<User> iterator = result1.iterator();
        while (iterator.hasNext()) {
            result.append(iterator.next().getName()+"\r\n");
        }

// Or alternatively do the same all at once (the "Fluent interface"):
        RealmResults<User> result2 = realm.where(User.class)
                .beginsWith("name", "Pie")
                .or()
                .beginsWith("name", "Peter")
                .findAll();
        result.append("size====" + result2.size()+"\r\n");
        Iterator<User> iterator1 = result2.iterator();
        while (iterator1.hasNext()) {
            result.append(iterator1.next().getName()+"\r\n");
        }
//        between()
//        greaterThan()
//        lessThan()
//        greaterThanOrEqualTo()
//        lessThanOrEqualTo()
//        equalTo()
//        notEqualTo()
//        contains()
//        beginsWith()
//        endsWith()
//        All fetches (including queries) are lazy in Realm, and the data is never copied.
//                常常我們會從資料庫取得資料，然後Display在UI上，
//        但礙於Realm的限制，我們無法從background Thread去Query，然後再到uiThread去Diaplay
//        但其實Realm也沒有必要這麼做，建議暫時都讓query都在uiThread上吧！
//        （已試過100000筆資料測試）
//        8.Sorting
//
//        很簡單，看就知道了
        RealmResults<User> result3 = realm.where(User.class).findAll();
        result3.sort("age"); // 升冪
//        result.sort("age", RealmResults.SORT_ORDER_DESCENDING);// 降冪
        result.append("total size====" + result3.size()+"\r\n");
        Iterator<User> iterator2 = result3.iterator();
        while (iterator2.hasNext()) {
            result.append(iterator2.next().getName()+"\r\n");
        }
//        9.Chaining Queries
//
//        有三種取得方式
//        findFirst() //第一筆
//        findAll() // 全部
//        findAllSorted() // 排序後的全部
//        10.Aggregation
//
//                還有4個很方便的方法
        long sum = result3.sum("age").longValue();
        long min = result3.min("age").longValue();
        long max = result3.max("age").longValue();
        double average = result3.average("age");

        result.append("\r\nsum==" + sum+"\r\n"+"min==" + min+"\r\n"+"max==" + max+"\r\n"+"average==" + average);
        show.setText(result.toString());
//        sum()
//        min()
//        max()
//        average()
//        11.Deletion

// All changes to data must happen in a transaction
//        realm.beginTransaction();

// remove single match
//        result.remove(0);
//        result.removeLast();

// remove a single object
//        Dog dog = result.get(5);
//        dog.removeFromRealm();

// Delete all matches
//        result.clear();

//        realm.commitTransaction();
    }


    //    4.onDestory
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
