package com.sand.pollingsdk;

import androidx.core.util.Consumer;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.sand.pollingsdk.models.Message;
import com.sand.pollingsdk.models.Messages;
import com.sand.pollingsdk.models.MessagesWithCount;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PollingService {

    private boolean appStarted = false;

    public PollingService(Consumer<Message> subscribedMethod) {
        this.subscribedMethod = subscribedMethod;

        // 700 delay
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new LifecycleObserver() {
            // TODO find a better way to ignore firing on resume at app start
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            public void connectListener() {
                if (appStarted) {
                    System.out.println("app resumed");
                    testPolling();
                }
                appStarted = true;
            }

            // TODO check if on pause calling when app closed
            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            public void disconnectListener() {
                disposable.dispose();
                System.out.println("app paused");
            }
        });
    }

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    Consumer<Message> subscribedMethod;
    private Disposable disposable;
    final int[] count = {0};

    public MessagesWithCount getMessage(Messages messages) {
        Message message = messages.getMessages().get(0);
        int value = message.getValue() * 10;

        MessagesWithCount messagesWithCount = new MessagesWithCount(value,
                Arrays.asList(new Message("Result " + message.getText(), value)));
        return messagesWithCount;
    }

    public void testPolling() {
        disposable = Observable.interval(1000, 1000,
                TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(aLong -> printCount(subscribedMethod), Throwable::printStackTrace);
    }

    private void printCount(Consumer<Message> subscribedMethod) {
        if (count[0] % 10 == 0 && count[0] != 0) {
            try {
                subscribedMethod.accept(new Message("polling cycle no => ", count[0]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("new value = " + count[0]);
        }
        count[0]++;
    }
}
