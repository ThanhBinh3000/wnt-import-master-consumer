package vn.com.gsoft.importmaster.service.impl;

import com.google.gson.Gson;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.gsoft.importmaster.entity.BacSies;
import vn.com.gsoft.importmaster.repository.BacSiesRepository;
import vn.com.gsoft.importmaster.service.BacSiesService;
import vn.com.gsoft.importmaster.model.dto.WrapData;

@Service
public class BacSiesServiceImpl implements BacSiesService {
    @Autowired
    private BacSiesRepository bacSiesRepository;
    @Override
    public BacSies save(String payload) {
        Gson gson = new Gson();
        TypeToken<WrapData<BacSies>> typeToken = new TypeToken<WrapData<BacSies>>() {};
        WrapData<BacSies> dataBacSis = gson.fromJson(payload, typeToken.getType());
        BacSies bacSies = dataBacSis.getData();
        return bacSiesRepository.save(bacSies);
    }
}
