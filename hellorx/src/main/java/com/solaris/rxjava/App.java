package com.solaris.rxjava;


import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException, IOException {

//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                for (File folder : folders) {
//                    File[] files = folder.listFiles();
//                    for (File file : files) {
//                        if (file.getName().endsWith(".png")) {
//                            final Bitmap bitmap = getBitmapFromFile(file);
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    imageCollectorView.addImage(bitmap);
//                                }
//                            });
//                        }
//                    }
//                }
//            }
//        }.start();



//        Observable.from(folders)
//                .flatMap((Func1) (folder) -> { Observable.from(file.listFiles()) })
//                .filter((Func1) (file) -> { file.getName().endsWith(".png") })
//                .map((Func1) (file) -> { getBitmapFromFile(file) })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe((Action1) (bitmap) -> { imageCollectorView.addImage(bitmap) });
//

//        最传统,最根本的样子
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("Hello,world!");
                        subscriber.onError(new Exception());
                        subscriber.onCompleted();
                    }
                }
        );

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("I'm finished");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        };

        myObservable.subscribe(mySubscriber);

////      去掉模板代码,更加简洁的实现
        Observable<String> myObservable1 = Observable.just("Hello,2 world");
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s+"2");
            }
        };
        myObservable1.subscribe(onNextAction);
//
////        串起来的样子
        Observable.just("Hello,3 world").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });
//
////        java8 lambdas
        Observable.just("Hello,4 world").subscribe(s -> System.out.println(s));
//
////        进阶之 Operators
        Observable.just("Hello,5 world")
                .map(new Func1<String, String>() {
            public String call(String s) {
                return s + " -Dan";
            }
        }).subscribe(s -> System.out.println(s));
//
//        Observable.just("Hello,51 world", "Hello,52 world").map(new Func1<String, String>() {
//            public String call(String s) {
//                return s + " -Dan";
//            }
//        }).subscribe(s -> System.out.println(s));
//
//        Observable.just("Hello,61 world", "Hello,62 world").map(s -> s + " -Dan").subscribe(s -> System.out.println(s));
//
////        神奇的map
        Observable.just("Hello, Map")
                .map(s -> s + " -Dan")
                .map(s -> s.hashCode())
                .map(i -> Integer.toString(i))
                .subscribe(s -> System.out.println(s));
//
//
////        神奇的flatmap
//        List<Student> students = initTestData();
//
//        Subscription subscribe = Observable.from(students)
//                .flatMap(new Func1<Student, Observable<Course>>() {
//                    @Override
//                    public Observable<Course> call(Student student) {
//                        System.out.println(student.getName() + "学习的课程有:");
//                        return Observable.from(student.getCourses());
//                    }
//                }).subscribe(new Action1<Course>() {
//                    @Override
//                    public void call(Course course) {
//                        System.out.println(course.getName());
//                    }
//                });
//
////        去掉王五的信息
//        Observable.from(students)
//                .filter(student -> !student.getName().equals("王五"))
//                .flatMap(new Func1<Student, Observable<Course>>() {
//                    @Override
//                    public Observable<Course> call(Student student) {
//                        System.out.println(student.getName() + "学习的课程有:");
//                        return Observable.from(student.getCourses());
//                    }
//                }).take(2).doOnNext(course -> {
//            System.out.println("------------------");
//        }).subscribe(course -> System.out.println(course.getName()));
//
//
//        //        神奇的Subject
        PublishSubject<Integer> subject = PublishSubject.create();
        subject.onNext(1);
        subject.subscribe(System.out::println);
        subject.onNext(2);
        subject.onNext(3);
        subject.onNext(4);
//
        ReplaySubject<Integer> s = ReplaySubject.create();
        s.subscribe(v -> System.out.println("Early:" + v));
        s.onNext(0);
        s.onNext(1);
        s.subscribe(v -> System.out.println("Late: " + v));
        s.onNext(2);
        System.out.println("-----------------------------------------");

//        ReplaySubject<Integer> s1 = ReplaySubject.createWithSize(2);
//        s1.onNext(0);
//        s1.onNext(1);
//        s1.onNext(2);
//        s1.subscribe(v -> System.out.println("Late: " + v));
//        s1.onNext(3);
//        s1.onCompleted();
//        System.out.println("-----------------------------------------");
//        ReplaySubject<Integer> s2 = ReplaySubject.createWithTime(150, TimeUnit.MILLISECONDS,
//                Schedulers.immediate());
//        s2.onNext(0);
//        Thread.sleep(100);
//        s2.onNext(1);
//        Thread.sleep(100);
//        s2.onNext(2);
//        s2.subscribe(v -> System.out.println("Late: " + v));
//        s2.onNext(3);
//        System.out.println("-----------------------------------------");
//
//        BehaviorSubject<Integer> s3 = BehaviorSubject.create();
//        s3.onNext(0);
//        s3.onNext(1);
//        s3.onNext(2);
//        s3.subscribe(v -> System.out.println("Late: " + v));
//        s3.onNext(3);
//        s3.onCompleted();
//
//        System.out.println("-----------------------------------------");
//        AsyncSubject<Integer> s4 = AsyncSubject.create();
//        s4.subscribe(v -> System.out.println(v));
//        s4.onNext(0);
//        s4.onNext(1);
//        s4.onNext(2);
//        s4.onCompleted();
//
//        System.out.println("-----------------------------------------");
//
//        Observable<Long> now = Observable.defer(() ->
//                Observable.just(System.currentTimeMillis()));
//
//        now.subscribe(System.out::println);
//        Thread.sleep(1000);
//        now.subscribe(System.out::println);
//
//        System.out.println("-----------------------------------------");
//        Observable<Integer> values = Observable.range(10, 15);
//        values.subscribe(i -> System.out.println(i));
//
//        System.out.println("-----------------------------------------");
//
////        Observable<Long> values1 = Observable.interval(1000, TimeUnit.MILLISECONDS);
////        values1.subscribe(
////                v -> System.out.println("Received: " + v),
////                e -> System.out.println("Error: " + e),
////                () -> System.out.println("Completed")
////        );
////        System.in.read();
//
////        System.out.println("-----------------------------------------");
//
//
////        Observable.timer(1, TimeUnit.SECONDS).subscribe(
////                v -> System.out.println("Received: " + v),
////                e -> System.out.println("Error: " + e),
////                () -> System.out.println("Completed")
////        );
////        System.in.read();
//
//
////        等待一段时间，然后开始按照间隔的时间一直发射数据：
//
////        Observable<Long> values2 = Observable.timer(2, 1, TimeUnit.SECONDS);
////        values2.subscribe(
////                v -> System.out.println("timer Received: " + v),
////                e -> System.out.println("Error: " + e),
////                () -> System.out.println("Completed")
////        );
////        System.in.read();
//
//        FutureTask<Integer> f = new FutureTask<Integer>(() -> {
//            Thread.sleep(2000);
//            return 21;
//        });
//        new Thread(f).start();
//
//        Observable.from(f).subscribe(
//                v -> System.out.println("Received: " + v),
//                e -> System.out.println("Error: " + e),
//                () -> System.out.println("Completed")
//        );
//
//        System.out.println("-----------------------------------------");
////        Filter
//        Observable.range(0, 10).filter(v -> v % 2 == 0).subscribe(
//                v -> System.out.println(v)
//        );
//
//        System.out.println("-----------------------------------------");
//        Observable.just("1", "1", "2", "3", "1", "2").distinct().subscribe(v -> System.out.println(v));
//
//        System.out.println("-----------------------------------------");
//
//
////        all 函数用来判断 observable 中发射的所有数据是否都满足一个条件。
//
//        Observable.just(2, 4, 2, 6, 8, 2).all(i -> i % 2 == 0).subscribe(v -> System.out.println(v));
//        System.out.println("-----------------------------------------");
////
////        Observable.interval(150, TimeUnit.MILLISECONDS).take(5).all(i -> i<3).subscribe(
////                v -> System.out.println("All: " + v),
////                e -> System.out.println("All: Error: " + e),
////                () -> System.out.println("All: Completed")
////        );
////        System.in.read();
////        System.out.println("-----------------------------------------");
//
//
////        exists
////
////        如果源 exists 发射的数据中有一个满足条件，则 exists 就返回 true。 exists 和 all 一样也是返回一个 Observable 而不是直接返回 布尔值。
////
//        Observable.range(0, 2)
//                .exists(i -> i > 2)
//                .subscribe(
//                        v -> System.out.println(v),
//                        e -> System.out.println("Error: " + e),
//                        () -> System.out.println("Completed")
//                );
//        Observable.range(0, 4)
//                .exists(i -> i > 2)
//                .subscribe(
//                        v -> System.out.println(v),
//                        e -> System.out.println("Error: " + e),
//                        () -> System.out.println("Completed")
//                );
//
//        System.out.println("-----------------------------------------");
////        isEmpty
////        顾名思义，判断一个 Observable 是否是空的，也就是没有发射任何数据就结束了。
////        Observable.timer(1000, TimeUnit.MILLISECONDS)
////                .isEmpty()
////                .subscribe(
////                        v -> System.out.println(v),
////                        e -> System.out.println("Error: " + e),
////                        () -> System.out.println("Completed")
////                );
////        System.in.read();
//
//        System.out.println("-----------------------------------------");
////        contains 使用 Object.equals 函数来判断源 Observable 是否发射了相同的数据。
////        只要遇到相同的数据，则 contains 就立刻返回。
////
////        defaultIfEmpty
////        如果你不想单独处理没有发射任何数据的情况（需要用 isEmpty 函数来检查是否为空），则可以使用 defaultIfEmpty 函数来强制一个空的 Observable 发射一个默认数据。
//
////        elementAt 从特定的位置选择一个数据发射。
//
////        sequenceEqual
////
////        本节最后这个操作函数是用来比较两个 Observable 发射的数据是否是一样的，同样位置的数据是一样的。要求两个 Observable 发射的数据个数是一样的并且每个位置上的数据也是一样的。 该函数内部用 Object.equals 来比较数据，当然你也可以自己指定一个比较函数。
//
//        Observable<String> strings = Observable.just("1", "2", "3");
//        Observable<Integer> ints = Observable.just(1, 2, 3);
//
//        Observable.sequenceEqual(strings, ints, (ss1, i) -> ss1.equals(i.toString()))
////Observable.sequenceEqual(strings, ints)
//                .subscribe(
//                        v -> System.out.println(v),
//                        e -> System.out.println("Error: " + e),
//                        () -> System.out.println("Completed")
//                );
//
//        System.out.println("-----------------------------------------");
////        Aggregation
////
////        前面介绍了如何过滤掉不需要的数据、如何根据各种条件停止发射数据、如何检查数据是否符合某个条件。这些操作对数据流来说都是非常有意义的。 本节介绍如何根据数据流中的数据来生成新的有意义的数据。
////        本节的操作函数会使用源 Observable 中的事件流中的数据，然后把这些数据转换为其他类型的数据。返回结果是包含一个数据的 Observable。
////
////        count 函数和 Java 集合中的 size 或者 length 一样。用来统计源 Observable 完成的时候一共发射了多少个数据。
////
////
////        first 类似于 take(1) , 发射 源 Observable 中的第一个数据。如果没有数据，则返回 ava.util.NoSuchElementException。还有一个重载的带有 过滤 参数，则返回第一个满足该条件的数据。
////
////        last 和 lastOrDefault 是和 first 一样的，区别就是当 源 Observable 完成的时候， 发射最后的数据。 如果使用重载的带 过滤参数的函数，则返回最后一个满足该条件的数据。
////
////
////        single 只会发射源 Observable 中的一个数据，如果使用重载的带过滤条件的函数，则发射符合该过滤条件的那个数据。和 first 、last 不一样的地方是，single 会检查数据流中是否只包含一个所需要的的数据，如果有多个则会抛出一个错误信息。所以 single 用来检查数据流中是否有且仅有一个符合条件的数据。所以 single 只有在源 Observable 完成后才能返回。
////
////        reduce
////
////        你可能从 MapReduce 中了解过 reduce。该思想是使用源 Observable 中的所有数据两两组合来生成一个单一的 数据。在大部分重载函数中都需要一个函数用来定义如何组合两个数据变成一个。
////
////        scan 和 reduce 很像，不一样的地方在于 scan会发射所有中间的结算结果。
////
////        collect
////        上面每一个值都创建一个新对象的性能是无法接受的。为此， Rx 提供了一个 collect 函数来实现该功能，该函数使用了一个可变的 accumulator 。需要通过文档说明你没有遵守 Rx 的原则使用不可变对象，避免其他人误解：
//
//        Observable.range(10, 5).collect(
//                () -> new ArrayList<Integer>(),
//                (acc, value) -> acc.add(value))
//                .subscribe(v -> System.out.println(v));
//
//
//        System.out.println("-----------------------------------------");
//
////        toList
////        前一个示例代码可以这样实现：
//
//        Observable.range(10, 5).toList()
//                .subscribe(v -> System.out.println(v));
//
//
//        System.out.println("-----------------------------------------");
//
////        toSortedList 和前面类似，返回一个排序后的 list，下面是该函数的定义：
//        Observable.range(10, 5)
//                .toSortedList((i1, i2) -> i2 - i1)
//                .subscribe(v -> System.out.println(v));
//
//        System.out.println("-----------------------------------------");
////        toMap 把数据流 T 变为一个 Map<TKey,T>。 该函数有三个重载形式：
//        Observable.just(
//                new Person("Will", 25),
//                new Person("Nick", 40),
//                new Person("Saul", 35)
//        ).toMap(person -> person.name)
////        ).toMap(person -> person.name,
////                person -> person.age)
//                .subscribe(v -> System.out.println(v));
//
//        System.out.println("-----------------------------------------");
//        System.out.println("-----------------------------------------");
//
//
//        Observable.just(
//                "first",
//                "second",
//                "third",
//                "forth",
//                "fifth",
//                "sixth"
//        ).groupBy(word -> word.charAt(0))
//                .flatMap(group -> group.last().map(v -> group.getKey() + ": " + v))
//                .subscribe(v -> System.out.println(v));
//
//
//        System.out.println("-----------------------------------------");
//
//
////        Observable.just(2).flatMap(i -> Observable.range(0,i))
////                .subscribe(v -> System.out.println(v));
//
//        System.out.println("-----------------------------------------");
////        concatMap
////
////        如果你不想把新 Observable 中的数据交织在一起发射，则可以选择使用 concatMap 函数。
////        该函数会等第一个新的 Observable 完成后再发射下一个 新的 Observable 中的数据。
////        Java
//
////        Observable.just(100, 150)
////                .concatMap(i -> Observable.interval(i, TimeUnit.MILLISECONDS).map(v -> i).take(3))
////                .subscribe(
////                        System.out::println,
////                        System.out::println,
////                        () -> System.out.println("Completed"));
////        System.in.read();
//
//        System.out.println("-----------------------------------------");
//
//        Inc index = new Inc();
//
//        Observable indexed = Observable.just("请", "不要", "有", "副作用").map(w -> {
//                    index.inc();
//                    return w;
//                });
//        indexed.subscribe(w -> System.out.println("1st observer: " + index.getCount() + ": " + w));
//        indexed.subscribe(w -> System.out.println("2nd observer: " + index.getCount() + ": " + w));
//
//
//
//        Observable<Indexed<String>> indexed1 =
//                Observable.just("No", "side", "effects", "please").scan(
//                        new Indexed<String>(0, null),
//                        (prev,v) -> new Indexed<String>(prev.index+1, v))
//                        .skip(1);
//        indexed1.subscribe(w -> System.out.println("1st observer: " + w.index + ": " + w.item));
//        indexed1.subscribe(w -> System.out.println("2nd observer: " + w.index + ": " + w.item));
//
//        System.out.println("-----------------------------------------");
//        Observable<String> obs = Observable.just("side", "effects");
//        obs.doOnEach(new PrintSubscriber("Log"))
//                .map(st1 -> (st1+"").toUpperCase())
//                .subscribe(new PrintSubscriber("Process"));
//
//        System.out.println("-----------------------------------------");
////        多线程
////        final BehaviorSubject<Integer> subject1 = BehaviorSubject.create();
////        subject1.subscribe(i -> {
////            System.out.println("Received " + i + " on " + Thread.currentThread().getId());
////        });
////
////        int[] i = {1}; // naughty side-effects for examples only ;)
////        Runnable r = () -> {
////            synchronized(i) {
////                System.out.println("onNext(" + i[0] + ") on " + Thread.currentThread().getId());
////                subject1.onNext(i[0]++);
////            }
////        };
////
////        r.run(); // Execute on main thread
////        new Thread(r).start();
////        new Thread(r).start();
//
//
////        subscribeOn 用来指定 Observable.create 中的代码在那个 Scheduler 中执行。即使你没有调用 create 函数，但是内部也有一个 create 实现。
////        observeOn 控制数据流的另外一端。你的 observer 如何收到事件。也就是在那个线程中回调 observer 的 onNext/onError/onCompleted 函数。
//
//
//        System.out.println("Main: " + Thread.currentThread().getId());
//
//        Observable.create(o -> {
//            System.out.println("Created on " + Thread.currentThread().getId());
//            o.onNext(1);
//            o.onNext(2);
//            o.onCompleted();
//        })
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(Schedulers.newThread())
//                .subscribe(ii -> {
//                    System.out.println("Received " + ii + " on " + Thread.currentThread().getId());
//                });
//
//        System.out.println("Finished main: " + Thread.currentThread().getId());
//
////        observeOn 只影响调用该函数以后的操作函数。你可以认为 observeOn 只是拦截了数据流并且对后续的操作有作用
//        Observable.create(o -> {
//            System.out.println("Created on " + Thread.currentThread().getId());
//            o.onNext(1);
//            o.onNext(2);
//            o.onCompleted();
//        })
//                .doOnNext(i1 ->
//                        System.out.println("Before " + i1 + " on " + Thread.currentThread().getId()))
//                .observeOn(Schedulers.newThread())
//                .doOnNext(i2 ->
//                        System.out.println("After " + i2 + " on " + Thread.currentThread().getId()))
//                .subscribe();
//
////        Schedulers
////
////        observeOn 和 subscribeOn 的参数为一个 Scheduler 对象。Scheduler 是用来协调任务执行的。 RxJava 包含了一些常用的 Scheduler，你也可以自定义 Scheduler。 通过调用 Schedulers 的工厂函数来获取标准的预定义的 Scheduler。
////        RxJava 内置的 Scheduler 有：
////– immediate 同步执行
////– trampoline 把任务放到当前线程的队列中，等当前任务执行完了，再继续执行队列中的任务
////– newThread 对于每个任务创建一个新的线程去执行
////– computation 计算线程，用于需要大量 CPU 计算的任务
////– io 用于执行 io 操作的任务
////– test 用于测试和调试
////        当前 computation 和 io 的实现是类似的，他们两个主要用来确保调用的场景，相当于文档说明，来表明你当前的任务是何种类型的。
////        大部分的 Rx 操作函数内部都使用了schedulers 。并且大部分的 Observable 操作函数也都有一个使用 Scheduler 参数的重载函数。通过重载函数可以指定该操作函数执行的线程。
////
//
//
//
////        如果你把上面的注释掉的代码 .subscribeOn(Schedulers.newThread()) 启用，这结果是这样的：
////        Java
////
////        Main: 1
////        Finished main: 1
////        Created on 11
////        Received 1 on 11
////        Received 2 on 11
////
////        这样 create 里面的 Lambda 表达式代码将会在 Schedulers.newThread() 返回的线程中执行。subscribe 不再是阻塞的了。后面的代码可以立即执行，而不用等待 subscribe 返回。
////        有些 Observable 内部会使用它们自己创建的线程。例如 Observable.interval 就是异步的。这种情况下，无需指定新的线程。
//
//
//
//        System.out.println("-----------------------------------------");
//
//        Observable.interval(100, TimeUnit.MILLISECONDS).take(3)
//                .materialize()
//                .subscribe(new PrintSubscriber("Materialize"));
//
//        System.out.println("-----------------------------------------");
//
//        Observable.interval(1, TimeUnit.MILLISECONDS)
//                .observeOn(Schedulers.io())
////                .subscribeOn(Schedulers.immediate())
//                .subscribe(
//                        i3 -> {
//                            System.out.println(i3);
//                            try {
//                                Thread.sleep(100);
//                            } catch (Exception e) { }
//                        });
//
//System.in.read();

    }

    private static List<Student> initTestData() {
        Course math = new Course("数学");
        Course yuwen = new Course("语文");
        Course dili = new Course("地理");
        Course lishi = new Course("历史");
        Course jisuanj = new Course("计算机");

        Student zhangssan = new Student("张三", math, yuwen);
        Student lisi = new Student("李四", math, yuwen, lishi);
        Student wangwu = new Student("王五", math, yuwen, dili);
        Student zhaoliu = new Student("赵六", math, yuwen, jisuanj);

        List<Student> students = new CopyOnWriteArrayList<>();
        students.add(zhangssan);
        students.add(lisi);
        students.add(wangwu);
        students.add(zhaoliu);

        return students;
    }
}

class Person {
    public final String name;
    public final Integer age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

class Inc {
    private int count = 0;
    public void inc() {
        count++;
    }
    public int getCount() {
        return count;
    }
}

class Indexed <T> {
    public final int index;
    public final T item;
    public Indexed(int index, T item) {
        this.index = index;
        this.item = item;
    }
}

class PrintSubscriber extends Subscriber{
    private final String name;
    public PrintSubscriber(String name) {
        this.name = name;
    }
    @Override
    public void onCompleted() {
        System.out.println(name + ": Completed");
    }
    @Override
    public void onError(Throwable e) {
        System.out.println(name + ": Error: " + e);
    }
    @Override
    public void onNext(Object v) {
        System.out.println(name + ": " + v);
    }
}

