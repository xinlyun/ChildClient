package com.lin.childclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Created by xinlyun on 16-1-14.
 */
public class ControlService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        PersonProbuf.AddressBook.Builder builder = PersonProbuf.AddressBook.newBuilder();
        PersonProbuf.Person.Builder person = PersonProbuf.Person.newBuilder();
        PersonProbuf.Person.PhoneNumber.Builder phoneNumber = PersonProbuf.Person.PhoneNumber.newBuilder();
        PersonProbuf.Person.CountryInfo.Builder countryInfo = PersonProbuf.Person.CountryInfo.newBuilder();
        phoneNumber.setNumber("13570495225");
        phoneNumber.setType(PersonProbuf.Person.PhoneType.WORK);
        countryInfo.setName("林子祥");
        countryInfo.setCode("123");
        countryInfo.setNumber(135);
        person.setName("linz");
        person.setId(12);
        person.setEmail("lingzixin01@163.com");
        person.addPhone(phoneNumber.build());
        builder.addPerson(person.build());
        PersonProbuf.AddressBook book = builder.build();



        return null;
    }



}
