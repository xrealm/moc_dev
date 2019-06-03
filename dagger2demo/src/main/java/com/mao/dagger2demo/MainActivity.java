package com.mao.dagger2demo;

import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.appcompat.app.AppCompatActivity;
import dagger.Lazy;

public class MainActivity extends AppCompatActivity {

    @PersonWithContext
    @Inject
    Person person;

    @PersonWithContext
    @Inject
    Person person2;

    @PersonWithName
    @Inject
    Person person3;

    @PersonWithName
    @Inject
    Person person4;

//    @Inject
//    Lazy<Person> lazyPerson;
//
//    @Inject
//    Provider<Person> providePerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AppComponent appComponent = DaggerAppComponent.builder().appModule(new AppModule(this))
//                .build();
        MainComponent component = DaggerMainComponent.builder()
                .mainModule(new MainModule()).appComponent(App.appComponent).build();
        component.inject(this);

//        Person person1 = lazyPerson.get();
//        Person person2 = lazyPerson.get();
//        Person person3 = providePerson.get();
//        Person person4 = providePerson.get();


        System.out.println(person);
        System.out.println(person2);
        System.out.println(person3);
        System.out.println(person4);

    }
}
