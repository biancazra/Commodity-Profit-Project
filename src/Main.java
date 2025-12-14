
 import java.io.File;
 import java.io.FileNotFoundException;
 import java.util.Scanner;

 public class Main {
         public static final int MONTHS =12;
         public static final int DAYS   =28;
         public static final int COMMODITY  =5;

     static final String[] COMMODITIES = {"Gold", "Oil", "Silver", "Wheat", "Copper"};

     static final String[] Months = {
             "January", "February", "March", "April", "May", "June",
             "July", "August", "September", "October", "November", "December"
     };

     static int[][][] profitData;

     public static void main(String[] args) {
         loadData();
     }

     static void loadData() {
         // 12 ay, 5 emtia, 28 gün
         profitData = new int[12][5][28];

         for (int month = 0; month < 12; month++) {
             try {
                 // Dosya yolunu oluştur
                 String fileName = "C:\\Users\\akgun\\IdeaProjects\\CommodityProject\\src\\Data_Files/" + Months[month] + ".txt";
                 File file = new File(fileName);

                 // Scanner ile dosyayı aç
                 Scanner scanner = new Scanner(file);

                 // Satır satır oku
                 while (scanner.hasNextLine()) {
                     String line = scanner.nextLine();
                     String[] parts = line.split(",");

                     if (parts.length == 3) {
                         try {
                             int day = Integer.parseInt(parts[0].trim()) - 1;
                             String commodity = parts[1].trim();
                             int profit = Integer.parseInt(parts[2].trim());

                             int commodityIndex = findCommodityIndex(commodity);

                             if (commodityIndex != -1 && day >= 0 && day < 28) {
                                 profitData[month][commodityIndex][day] = profit;
                             }
                         } catch (NumberFormatException e) {
                             // Hatalı satırı atla
                             continue;
                         }
                     }
                 }

                 scanner.close();

             } catch (FileNotFoundException e) {
                 System.err.println("Hata: " + Months[month] + ".txt dosyası bulunamadı!");
             }

         }

     }
     static int findCommodityIndex(String commodity){
         for (int i = 0; i < COMMODITIES.length; i++) {
             if (COMMODITIES[i].equalsIgnoreCase(commodity)) {
                 return i;
             }
         }
         return -1;
     }
     static String mostProfitableCommodityInMonth(int month){  //Ay numarasının doğru aralıkta olup olmadığını kontrol ediyorum
        if (month <0 || month >11 ) {
          return "INVALID_MONTH";
        }
         int maxProfit = Integer.MIN_VALUE; // EN YÜKSEK KARI BURDA TUTUYORUM
         int bestCommodityIndex = -1;  //en karlı emtianın sırasını burada tutuyorum

             // 5 emtiayı gezerek kontrol ediyorum
         for (int commodity = 0; commodity<5 ; commodity++){
             int totalProfit = 0;
             //Bu emtianın ayın ayın 28 günündeki karlarını topluyorum
             for (int day = 0; day<28; day++){
                 totalProfit += profitData [month][commodity][day];
             }
             if (totalProfit>maxProfit){
                 maxProfit=totalProfit;
                 bestCommodityIndex=commodity;
             }
         }
         //en karlı emtianın adını ve karını döndürüyorum
        return COMMODITIES[bestCommodityIndex] + " " + maxProfit ;
     }
     //belirtilen ay ve gün için tüm emtiaların karını hesaplıyorum  ay (0=ocak 11=aralık) gün (1-28)
      static int totalProfitOnDay (int month,int day){
        if (month<0 || month >11 || day<1 || day>28){
            return -99999;
        }
        int totalProfit = 0;
        int dayIndex =day-1; // günü 1-28den 0-27ye dönüştürüyorum
          for (int commodity =0; commodity<5 ; commodity++ ){
              totalProfit +=profitData [month][commodity][dayIndex];
          }
          return totalProfit;
    }
    //belirtilen emtia için belirtilen gün aralığında toplam karı hesaplıyorum ("Gold") fromDay 1-28 toDay 1-28
     static int commodityProfitInRange (String commodity, int fromDay, int toDay){
         //emtia adının indexini buluyorum
         int commodityIndex = findCommodityIndex(commodity);
         //emtia adı v e gün aralığının doğru olup olmadığını ontrol ediyorum
         if (commodityIndex == -1 || fromDay < 1 || toDay < 1 || fromDay > 28 || toDay > 28 || fromDay > toDay ){
             return -99999;
         }
         int totalProfit = 0;
         //12 ayın hepsi için belirtilen gün aralığındaki karı topluyorum
         for (int month = 0; month<12 ; month++){
             //fromDayden toDaye kadar (0-indexed olarak dolaşıyorum)
             for (int day = fromDay -1 ; day< toDay ;day++){
                 totalProfit += profitData[month][commodityIndex][day];
             }
         }
         return totalProfit;
     }
     // belirtilen ay içindeki en çok kar yapılan günü buluyorum
     static int bestDayOfMonth(int month) {
         if (month < 0 || month > 11) {
             return -1;
         }
         int maxProfit = Integer.MIN_VALUE; //en yüksek karı burada tutuyotrum
         int bestDay = -1; //en karlı gün de burada

         // ayın her günü için karı hesaplıoruz
         for (int day = 1; day <= 28; day++) {
             int dailyProfit = totalProfitOnDay(month, day);
             if (dailyProfit > maxProfit) {
                 maxProfit = dailyProfit;
                 bestDay = day;
             }
         }
     return bestDay;
     }
     //Belirtilen emtia için en karlı ayı buluyorum
     static String bestMonthForCommodity(String commodity){
         //emtia adının indexini buluyorum
         int commodityIndex = findCommodityIndex(commodity);
         //emtia adının doğru olup olmadığını kontrol ediyorum
         if (commodityIndex == 1){
             return "INVALID_COMMODITY";
         }
         int maxProfit = Integer.MIN_VALUE; //en yüksek karı tutuyorum
         int bestMonthIndex = -1; //en karlı ayın sırasını burada tutuyorum
         //12 ayın hepsini dolaşarak kontrol ediyorum
         for (int month = 0; month<12; month++){
             int monthlyProfit=0;
             //bu ayın 28 günü için total profiti hesaplıyorum
             for (int day=0;day<=28;day++){
                 monthlyProfit += profitData[month][commodityIndex][day]
             }
             if(monthlyProfit>maxProfit){
                 maxProfit=monthlyProfit;
                 bestMonthIndex=month;
             }
         }
         //en karlı ayın adı:
         return Months[bestMonthIndex];
     }

 }
