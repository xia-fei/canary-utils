package com.wing.insight.test;

import com.qccr.commons.ro.PagedDataRO;
import com.qccr.knife.result.Result;
import com.wing.insight.Mock;
import com.wing.insight.test.bean.CatRO;
import com.wing.insight.test.bean.Dog;
import com.wing.insight.test.bean.NestRO;
import com.wing.insight.test.bean.Person;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockFacade {

    @Mock
    public Result<NestRO> nestROResult(){
        return null;
    }
    @Mock
    public Result<Person> person(){
        return null;
    }
    @Mock
    public Result<CatRO> roTest(){
        return null;
    }

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

    @Mock
    public Dog list4(){
        return null;
    }

    @Mock
    public List<Dog> list5(){
        return null;
    }
}
