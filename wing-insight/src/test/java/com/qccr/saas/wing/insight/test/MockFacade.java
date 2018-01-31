package com.qccr.saas.wing.insight.test;

import com.qccr.commons.ro.PagedDataRO;
import com.qccr.knife.result.Result;
import com.qccr.saas.wing.insight.Mock;
import com.qccr.saas.wing.insight.test.bean.Dog;
import com.qccr.saas.wing.insight.test.bean.Person;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockFacade {

    @Mock
    public Result<List<Person>> list(){
        return null;
    }


    @Mock
    public Result<Dog> list2(){
        return null;
    }

    @Mock
    public Result<PagedDataRO<Person>> list3(){
              return null;
    }
}
