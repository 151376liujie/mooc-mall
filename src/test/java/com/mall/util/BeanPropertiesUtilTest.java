package com.mall.util;

import com.mall.common.Constants;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Author: jonny
 * Time: 2017-05-17 12:40.
 */
public class BeanPropertiesUtilTest {


    @Test
    public void testCopyProperties() throws InvocationTargetException, IllegalAccessException {
        Person person = new Person();
        person.setId(1);
        person.setName("jonny");
        person.setAge(20);
        person.setSex(2);

        System.out.println(person);

        Student student = new Student();
        BeanUtils.copyProperties(student,person);

        System.out.println(student);
    }

    @Test
    public void testDateFormat(){
        String format = DateFormatUtils.format(new Date(), Constants.DATE_TIME_FORMAT_PATTERN);
        System.out.println(format);
    }

}
