 # [PlantApp](https://www.notion.so/cea50616f805494b9018a1494b43b282)
자신이 키우는 식물을 관리할 수 있는 어플리케이션 

### 목차 
[1. 기능 및 핵심 코드](#important)


[2. 트러블 슈팅](#troubleshooting) 


[3. 사용한 개발 도구 및 라이브러리](#tool)


[4. 완성된 App 이미지](#image)


# important
#### 기능
1. 카카오 로그인을 사용해서 회원을 관리합니다.
2. 사계절 날씨 정보를 받아 물 주기를 계산합니다. (여름이면 주기가 짧아지고 겨울이면 주기가 길어짐)
3. 식물의 물주기를 완료하면 새로 업데이트 됩니다.  


#### 식물 주기 저장 로직  

   controller
   ``` JAVA
    @ApiOperation(value = "마이가든 저장")
    @PostMapping(value = "/user/myPlant/save")
    public Header<Long> saveMyPlant(@ApiParam(value = "닉네임, 식물 명, 식물 사진 url, 식물 영 명, 입양 날짜, 물 날짜, 비료 날짜, 키우는 장소", required = true)
                                    @Validated @RequestBody MyPlantSaveDto myPlantSaveDto) throws IOException {
        //사용자 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        Long myPlant = myPlantService.saveMyPlant(myPlantSaveDto, account);
        return Header.OK(myPlant);
    }
   ```


   service
   ```JAVA
   @Transactional
   public Long saveMyPlant(MyPlantSaveDto myPlantSaveDto, Account account) throws IOException{

       //식물에 유저 저장
       MyPlant myPlant = myPlantSaveDto.entity();
  
       //식물 이름 찾기
       PlantWeather plantWeather = plantWeatherDataRepository.findByPlantName(myPlantSaveDto.getPlantName()).orElseThrow(NullPointerException::new);
       myPlant.addPlantName(plantWeather.getPlantName());
       myPlant.addPlantWeather(plantWeather);

       //물 주기 계산
       createWater(myPlantSaveDto.getCreateWater(), plantWeather, myPlant);
       String place = myPlantSaveDto.getPlace();
       myPlant.addAccount(account);
       myPlantRepository.save(myPlant);
       return myPlant.getId();
    }

    //식물 물 주기 일정 생성
    public void createWater(LocalDate day, PlantWeather plantName, MyPlant myPlant) {  

        //식물 계절별 물 주기 값 리턴
        int waterPeriod = findWaterSeason(day, plantName);

        //기준 날짜 만들기 = 처음 물준 날 + waterPeriod
        LocalDate standardDay = day.plusDays(waterPeriod); 

        //기준 날짜 +-일 일정 생성
        int period = createPeriod(waterPeriod);
        LocalDate minusDays = standardDay.minusDays(period);
        LocalDate plusDays = standardDay.plusDays(period);

        //주기 날짜 저장
        myPlant.saveStandardWater(minusDays, standardDay, plusDays);
   }

    //식물 사계절
    public int findWaterSeason(LocalDate weatherDate, PlantWeather plantWeather) {
  
        //현재 계절 추출
        int month = weatherDate.getMonthValue();
        switch (month){
           //봄 2~4
           case 2: case 3: case 4:
              int spring =  Integer.parseInt(plantWeather.getSpringWatering());
              return spring;
           //여름 5~7
           case 5: case 6: case 7:
              //중기 강수 확률 & 평균 온도 확인 후, 물주기 일정 변경
              int summer =  Integer.parseInt(plantWeather.getSummerWatering());
              return summer;
           //가을 8~10
           case 8: case 9: case 10:
               int autumn = Integer.parseInt(plantWeather.getAutumnWatering());
               return autumn;
           //겨울
           default:
              int winter = Integer.parseInt(plantWeather.getWinterWatering());
              return winter;
         }  
    }

    //기준 날짜에 해당 계절 주기의 만들기.
    private int createPeriod(int waterPeriod) {
          switch (waterPeriod){
             case 3: case 5:
             return 1;
             default:
             return 2;
          }
      }
  ```


# troubleshooting
#### 1. 사진 업로드 깨짐 현상
  여러 개의 사진을 S3에 업로드할 때마다 두 번째 사진부터 파일이 깨져서 올라갔습니다. 원인을 찾아보니 ObjectMetadata에서 setContentType()메서드에서 기존 파일의 값이 덮어쓰기 됩니다.

  ##### 에러 코드
  ```java
  ObjectMetadata omd = new ObjectMetadata();
  omd.setContentType(file.getContentType());
  omd.setContentLength(file.getSize());
  omd.setHeader("filename", file.getOriginalFilename());
  ```
  
  ##### 수정된 코드
  ```JAVA
  private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }
  ```
  업로드할 때, FileOutputStream()을 사용해서  MultipartFile  →  File 파일을 복사한 후 올립니다.
  

   
#### 2. 언더스코어 에러
  엔티티에 언더스코어를 사용하니 에러가 발생했습니다.
   
  ##### 에러 코드
  ```java
  @Entity
  @Getter
  @NoArgsConstructor(access = AccessLevel.PUBLIC)
  @AllArgsConstructor
  public class Account extends BaseTimeEntity {

	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  @Column(name = "account_id")
	  private Long id;

	  @Size(min = 2, max = 15)
	  private String nick_name;
  ```

  ```java
  public interface AccountRepository extends JpaRepository<Account, Long> {
      Optional<Account> findAccountBynickName(String nick_name);
  }
  ```

   해결 
   Account 타입의 닉네임 프로퍼티를 찾을 수 없다고 말해주었습니다. 에러를 찾아보니 Spring-Data-JPA에서는 언더스코어(_)가 프로퍼티을 찾기 위한 탐색 경로를 지정하는 예약어입니다.


3. objectMapper 에러
   리액트 네이티브에서 로그인을 위해 유저정보를 json을 넘기던 중, No content to map due to end-of-input 발생했습니다. 원인을 찾아보니 objectMapper에서 발생한 에러였습니다.

   ##### 에러 코드
   ```java
   //accountDto json -> 객체로 변환.
   AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);
   ```

   ##### 수정된 코드
   ```JAVA
   ServletInputStream inputStream = request.getInputStream();
   String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
   LoginDto loginDto = objectMapper.readValue(messageBody, LoginDto.class);
   ```
   json으로 넘어온 정보들을 읽을 때 발생한 문제였습니다. 그래서 StreamUtils 유틸 클래스의 copyToString()를 사용해서 InputStream/Output Stream으로 String으로 변환했습니다. 


# tool
사용 언어
```
- JAVA 8
- js
```

사용 기술
```
- spring Boot
- spring Security
- spring data jpa
- H2
- Mysql
- Querydsl
```

라이브러리
```
- lombok
- gradle
- react-Native
```

# image
<div align="center"> 
  <img alt="1" src="https://github.com/greeneryjin/PlantApp/assets/87289562/b1016d15-ae77-4751-8c3c-d7b97e3f152d" width = 20%>
  <img alt="22" src="https://github.com/greeneryjin/PlantApp/assets/87289562/ef2b9d17-e838-48c3-bb77-706e024c01e1" width = 20%>
  <img alt="캡처" src="https://github.com/greeneryjin/PlantApp/assets/87289562/7c91fb80-cef9-4aab-83d4-e1e57152470c" width = 20%>
  <img alt="333" src="https://github.com/greeneryjin/PlantApp/assets/87289562/42c32ba0-0065-494d-b1ff-3d67f5923752" width = 20%>
</div>

