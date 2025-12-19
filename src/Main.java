import java.util.*;
import java.io.*;

public class Main {
    public static final int MONTHS = 12;
    public static final int DAYS = 28;
    public static final int COMMODITY = 5;

    static final String[] COMMODITIES = {"Gold", "Oil", "Silver", "Wheat", "Copper"};

    static final String[] Months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    // 12 ay, 5 emtia, 28 gün
    public static int[][][] profitData = new int[12][5][28];

    public static void main(String[] args) {
        loadData();
    }

    public static void loadData() {

        for (int month = 0; month < 12; month++) {
            try {
                // Scanner ile dosyayı aç

                File file = new File(Months[month] + ".txt");
                Scanner sc = new Scanner(file);

                // Satır satır oku
                while (sc.hasNext()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");

                    if (parts.length == 3) {
                        try {
                            int day = Integer.parseInt(parts[0].trim()) - 1;
                            String commodity = parts[1].trim();
                            int profit = Integer.parseInt(parts[2].trim());

                            int commodityIndex = -1;
                            for(int i = 0; i < COMMODITY; i++){
                                if (commodity.equals(COMMODITIES[i])){
                                    commodityIndex = i;
                                    break;
                                }
                            }

                            if (commodityIndex != -1 && day >= 0 && day < 28) {
                                profitData[month][commodityIndex][day] = profit;
                            }
                        } catch (NumberFormatException e) {
                            // Hatalı satırı atla
                            continue;
                        }
                    }
                }

                sc.close();

            } catch (Exception e) {
                System.err.println("Hata: " + Months[month] + ".txt dosyası bulunamadı!");
            }

        }

    }

    public static String mostProfitableCommodityInMonth(int month) {  //Ay numarasının doğru aralıkta olup olmadığını kontrol ediyorum
        if (month < 0 || month > 11) {
            return "INVALID_MONTH";
        }
        int maxProfit = Integer.MIN_VALUE; // EN YÜKSEK KARI BURDA TUTUYORUM
        int bestCommodityIndex = -1;  //en karlı emtianın sırasını burada tutuyorum

        // 5 emtiayı gezerek kontrol ediyorum
        for (int commodity = 0; commodity < 5; commodity++) {
            int totalProfit = 0;
            //Bu emtianın ayın ayın 28 günündeki karlarını topluyorum
            for (int day = 0; day < 28; day++) {
                totalProfit += profitData[month][commodity][day];
            }
            if (totalProfit > maxProfit) {
                maxProfit = totalProfit;
                bestCommodityIndex = commodity;
            }
        }
        //en karlı emtianın adını ve karını döndürüyorum
        return COMMODITIES[bestCommodityIndex] + " " + maxProfit;
    }

    //belirtilen ay ve gün için tüm emtiaların karını hesaplıyorum  ay (0=ocak 11=aralık) gün (1-28)
    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month > 11 || day < 1 || day > 28) {
            return -99999;
        }
        int totalProfit = 0;
        int dayIndex = day - 1; // günü 1-28den 0-27ye dönüştürüyorum
        for (int commodity = 0; commodity < 5; commodity++) {
            totalProfit += profitData[month][commodity][dayIndex];
        }
        return totalProfit;
    }

    //belirtilen emtia için belirtilen gün aralığında toplam karı hesaplıyorum ("Gold") fromDay 1-28 toDay 1-28
    public static int commodityProfitInRange(String commodity, int fromDay, int toDay) {
        //emtia adının indexini buluyorum
        int commodityIndex = -1;
        for(int i = 0; i < COMMODITY; i++){
            if (commodity.equals(COMMODITIES[i])){
                commodityIndex = i;
                break;
            }
        }
        //emtia adı v e gün aralığının doğru olup olmadığını kontrol ediyorum
        if (commodityIndex == -1 || fromDay < 1 || toDay < 1 || fromDay > 28 || toDay > 28 || fromDay > toDay) {
            return -99999;
        }
        int totalProfit = 0;
        //12 ayın hepsi için belirtilen gün aralığındaki karı topluyorum
        for (int month = 0; month < 12; month++) {
            //fromDayden toDaye kadar (0-indexed olarak dolaşıyorum)
            for (int day = fromDay - 1; day < toDay; day++) {
                totalProfit += profitData[month][commodityIndex][day];
            }
        }
        return totalProfit;
    }

    // belirtilen ay içindeki en çok kar yapılan günü buluyorum
    public static int bestDayOfMonth(int month) {
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
    public static String bestMonthForCommodity(String commodity) {
        //emtia adının indexini buluyorum
        int commodityIndex = -1;
        for(int i = 0; i < COMMODITY; i++){
            if (commodity.equals(COMMODITIES[i])){
                commodityIndex = i;
                break;
            }
        }
        //emtia adının doğru olup olmadığını kontrol ediyorum
        if (commodityIndex == -1) {
            return "INVALID_COMMODITY";
        }
        int maxProfit = Integer.MIN_VALUE; //en yüksek karı tutuyorum
        int bestMonthIndex = -1; //en karlı ayın sırasını burada tutuyorum
        //12 ayın hepsini dolaşarak kontrol ediyorum
        for (int month = 0; month < 12; month++) {
            int monthlyProfit = 0;
            //bu ayın 28 günü için total profiti hesaplıyorum
            for (int day = 0; day < 28; day++) {
                monthlyProfit += profitData[month][commodityIndex][day];
            }
            if (monthlyProfit > maxProfit) {
                maxProfit = monthlyProfit;
                bestMonthIndex = month;
            }
        }
        //en karlı ayın adı:
        return Months[bestMonthIndex];
    }

    //belirtilen emtia için en uzun KAYBETME serisini buluyorum
    // kaybetme günü = kar negatif olan gün
    public static int consecutiveLossDays(String commodity) {
        // Emtia adının indexini buluyorum
        int commodityIndex = -1;
        for(int i = 0; i < COMMODITY; i++){
            if (commodity.equals(COMMODITIES[i])){
                commodityIndex = i;
                break;
            }
        }

        // Emtia adının doğru olup olmadığını kontrol ediyorum
        if (commodityIndex == -1) {
            return -1;
        }

        int maxConsecutiveLosses = 0; // En uzun kaybetme serisinin uzunluğu
        int currentLossStreak = 0; // Şu anda devam eden kaybetme serisi

        // 12 ay boyunca tüm günleri dolaşıyorum
        for (int month = 0; month < 12; month++) {
            for (int day = 0; day < 28; day++) {
                // Eğer o günün kârı negatifse (zarar ediyor)
                if (profitData[month][commodityIndex][day] < 0) {
                    currentLossStreak++; // Kaybetme serisini artırıyorum
                } else {
                    // Kâr yapılan gün olunca, seriyi kontrol ediyorum
                    if (currentLossStreak > maxConsecutiveLosses) {
                        maxConsecutiveLosses = currentLossStreak;
                    }
                    currentLossStreak = 0;
                }
            }
        }

        // Yıl sonunda devam eden seriyi de kontrol ediyorum
        if (currentLossStreak > maxConsecutiveLosses) {
            maxConsecutiveLosses = currentLossStreak;
        }

        return maxConsecutiveLosses;
    }



    //belirtilen emtia için karı belirtilen eşikten fazla olan günleri sayıyorum
    public static int daysAboveThreshold(String commodity, int threshold) {
        //emtia adının indexini buluyorum
        int commodityIndex = -1;
        for(int i = 0; i < COMMODITY; i++){
            if (commodity.equals(COMMODITIES[i])){
                commodityIndex = i;
                break;
            }
        }
        //emtia adının doğru olup olmadığını kontrol ediyorum
        if (commodityIndex == -1) {
            return -1;
        }
        int count = 0; //eşikten fazla kar yapan günlerin sayısı
        //12 ay boyunca tüm günleri dolaşıyorum
        for (int month = 0; month < 12; month++) {
            for (int day = 0; day < 28; day++) {
                //eğer o günün karı eşikten fazla ise sayyıy arttırıyorum
                if (profitData[month][commodityIndex][day] > threshold) {
                    count++;
                }
            }
        }
        return count;
    }

    //belirtilen ay için ardışık iki günün karı arasındaki en büyük farkı buluyorum
    public static int biggestDailySwing(int month) {
        // Ay numarasının doğru olup olmadığını kontrol ediyorum
        if (month < 0 || month > 11) {
            return -99999;
        }

        int maxSwing = 0; // En büyük fark

        // Ardışık gün çiftlerini dolaşıyorum (1-27 ile 2-28)
        for (int day = 1; day < 28; day++) {
            int profit1 = totalProfitOnDay(month, day); // Gün 1'in toplam kârı
            int profit2 = totalProfitOnDay(month, day + 1); // Gün 2'nin toplam kârı

            // İki günün kârı arasındaki farkı hesaplıyorum (mutlak değer)
            int swing = Math.abs(profit1 - profit2);

            // Eğer bu fark şu ana kadarki en büyükten fazlaysa, güncellerim
            if (swing > maxSwing) {
                maxSwing = swing;
            }
        }

        return maxSwing;
    }


    //iki emtianın 12 ay boyunca toplam karını karşılaştırıyorum
    public static String compareTwoCommodities(String c1, String c2) {
        //her iki emtianın indexini buluyorum
        int index1 = -1;
        for(int i = 0; i < COMMODITY; i++){
            if (c1.equals(COMMODITIES[i])){
                index1 = i;
                break;
            }
        }
        int index2 = -1;
        for(int i = 0; i < COMMODITY; i++){
            if (c2.equals(COMMODITIES[i])){
                index2 = i;
                break;
            }
        }
        //emtia adlarının doğru olup olmadığını kontrol ediyorum
        if (index1 == -1 || index2 == -1) {
            return "INVALID_COMMODITY";
        }
        int profit1 = 0;  //1. emtianın toplam karı
        int profit2 = 0;  //2. emtianın toplam karı

        //ilk emtianın 12 ayda toplam karını hesaplıyorum
        for (int month = 0; month < 12; month++) {
            for (int day = 0; day < 28; day++) {
                profit1 += profitData[month][index1][day];

            }
        }
        //2. emtianın 12 aydantoplam karını hesaplıyorum
        for (int month = 0; month < 12; month++) {
            for (int day = 0; day < 28; day++) {
                profit2 += profitData[month][index2][day];
            }
        }
        //karları karşılaştırıp sonuç döndürüyorum
        if (profit1 > profit2) {
            return c1 + " is better by " + (profit1 - profit2);
        } else if (profit2 > profit1) {
            return c2 + " is better by " + (profit2 - profit1);
        } else {
            return "Equal";
        }
    }

    //belirtilen ay için 4 hafta içindeki en karlı haftayı buluyorum (1-7)->()->(8-14)->(15-21)->(22-28)
    public static String bestWeekOfMonth(int month) {
        //ay numarasının doğru olup olmadığını kontrol ediyorum
        if (month < 0 || month > 11) {
            return "INVALID_MONTH";
        }
        int maxProfit = Integer.MIN_VALUE; //En yüksek haftalık kar
        int bestWeek = -1; // en karlı haftanın numarası -----neden -1 ------
        //4hafta dolaşarak kontrol ediyorum
        for (int week = 1; week <= 4; week++) {
            int weeklyProfit = 0;
            int startDay = (week - 1) * 7 + 1; //haftanın başlangıç günü
            int endDay = week * 7;       //haftanın bitiş günü
            //haftanın her günü için toplam karı hesaplıyorum
            for (int day = startDay; day <= endDay; day++) {
                weeklyProfit += totalProfitOnDay(month, day);
            }
            //eğer bu haftanın karı şuana kadar en yüksekse güncellensin
            if (weeklyProfit > maxProfit) {
                maxProfit = weeklyProfit;
                bestWeek = week;

            }
        }

        return "Week " + bestWeek;
    }

}

