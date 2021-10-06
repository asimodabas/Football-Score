import java.nio.file.*;

public class skor {

	//dosyayi okuyup yeni sat�r karakteri ile b�len fonksiyon
	public static String[] dosyaOku(String dosyaAdi)throws Exception { 
		String[] sonuclar = new String(Files.readAllBytes(Paths.get(dosyaAdi))).split("\r\n"); 
	    return sonuclar; 
	}
	
	
	
	public static void main(String[] args) throws Exception {
		String[] takimlar = new String[5];				//t�m tak�mlar�n isimleri
		String[][] siraliTakimlar = new String[99][5];	//isimlerin hafta hafta s�ralanm�� hali
		Mac[][][] maclar = new Mac[99][5][99];			//tak�m ve hafta baz�nda ma�lar ([hafta][tak�m][ma�])
		int[][] takimMacSayisi = new int[99][5];		//mac dizisini d�zenleyebilmek i�in saya� de�i�keni
		
		String[] macSonuclari = dosyaOku("src/turnuva.txt"); 	//ma� sonu�lar� metin belgesinden okunup macSonuclari de�i�kenine aktar�l�yor
		int macSayisi = macSonuclari.length;					//toplam ma� say�s�
		int haftaSayisi = 0;									//en son hafta say�s�
		
		
		//turnuva.txt dosyasi ile al�nan tak�mlar�n isimleri takimlar dizisine atan�yor
		for(int j = 0; j<macSayisi; j++) {
			String[] macDegerleri = macSonuclari[j].split(" ");	//al�nan sat�r bo�luklardan b�l�n�yor
			//5 tak�m olaca��ndan d�ng� 5 kez tekrar edecek
			for(int i = 0;i<5;i++) {
				if(takimlar[i] == macDegerleri[1]) {	//e�er ayn� isim daha �nc eeklenmi�se d�ng� bitiriliyor
					break;
				}
				else if(takimlar[i] == null) {			//dizide bo� bir eleman varsa o elemana tak�m ismi atan�yor
					takimlar[i] = macDegerleri[1];
					break;
				}
			} 
			//ayn� i�lemler di�er tak�m ismi i�in de yap�l�yor(macDegerleri[1] birinci tak�m, macDegerleri[2] is 2. tak�m ismidir)
			for(int i = 0;i<5;i++) {
				if(takimlar[i] == macDegerleri[2]) {
					break;
				}
				else if(takimlar[i] == null) {
					takimlar[i] = macDegerleri[2];
					break;
				}
			} 
		}
		 
		//al�nnan t�m ma�lar tek tek Mac s�n�f�na �evrilerek maclar dizisine kaydedilecek
		 for(int j = 0; j<macSayisi; j++) { 
			 String[] macDegerleri = macSonuclari[j].split(" ");
			 
			 //ma��n hangi haftaya aail oldu�u verisi al�n�yor
			 int hafta = Integer.parseInt(macDegerleri[0]);
			 //en b�y�k hafta de�eri haftaSayisi de�i�kenine saklanacak
			 if(hafta > haftaSayisi) {
				 haftaSayisi = hafta;
			 }
			 
			 //tak�mlar�n skor de�erleri al�n�yor
			 int takim1Skor = Integer.parseInt(macDegerleri[3].split("-")[0]);
			 int takim2Skor = Integer.parseInt(macDegerleri[3].split("-")[1]);
	
			 //ma�ta ismi ge�en tak�mlar "takimlar" dizisindeki isimlerle kar��la�t�r�l�yor. �simlerin e�le�ti�i i de�eri ile i�lem yap�l�yor
			 for(int i = 0; i<5; i++) {
				//tak�mlar�n skorlar� kar��la�t�r�larak yenme-yenilme durumu hesaplan�yor
				 if(macDegerleri[1].matches(takimlar[i])) {
				
					 //tak�m kaybettiyse, Kaybedilen s�n�f�ndan bir obje olu�turulup diziye ekleniyor
					 if(takim1Skor < takim2Skor) {
						 maclar[hafta][i][takimMacSayisi[hafta][i]] = new Kaybedilen(takim1Skor,takim2Skor);
					 }
					 //kazand�ysa, Kazanma objesi ekleniyor
					 else if(takim1Skor > takim2Skor) {
						 maclar[hafta][i][takimMacSayisi[hafta][i]] = new Kazanma(takim1Skor,takim2Skor);
					 }
					 //iki durumda ge�ersizse Berabere tipindeki obje ekleniyor
					 else {
						 maclar[hafta][i][takimMacSayisi[hafta][i]] = new Berabere(takim1Skor,takim2Skor);
					 }
					 //hangi haftada,hangi tak�m�n hangi ma�� oldu�unu belirleyebilmek i�in dizinin bu de�eri 1 art�r�l�yor
					 takimMacSayisi[hafta][i] += 1; 
				 }
				 //ayn� i�lemler ma�taki 2. tak�m i�in yap�l�yor
				 else if(macDegerleri[2].matches(takimlar[i])) {
					
					 if(takim1Skor < takim2Skor) {
						 maclar[hafta][i][takimMacSayisi[hafta][i]] = new Kazanma(takim2Skor,takim1Skor);
					 }
					 else if(takim1Skor > takim2Skor) {
						 maclar[hafta][i][takimMacSayisi[hafta][i]] = new Kaybedilen(takim2Skor,takim1Skor);
					 }
					 else {
						 maclar[hafta][i][takimMacSayisi[hafta][i]] = new Berabere(takim1Skor,takim2Skor);
					 }
					 takimMacSayisi[hafta][i] += 1;
				 }
			 }
		 }
		 
		 //S�ralamada kullan�lacak isimler i�in siraliTakimlar dizisi olu�turuldu
		 //Bu diziye ba�lang�� de�eri olarak en ba�ta al�nan tak�m isimleri atan�yor
		 //bu dizide hafta hafta tak�m s�ralamala� olacak
		 
		 for(int i = 1;i<=haftaSayisi;i++) {
			 for(int j = 0;j<5;j++) {
				 siraliTakimlar[i][j] = takimlar[j];
				 }
			 }
		

		 //tak�mlar�n toplam puanlar�na g�re b�y�kten k����e s�ralana�yor
		 //s�ralama her hafta i�in farkl� olaca��ndan d�ng� hafta say�s� kadar tekrar edecel
		 for(int i = 1;i<=haftaSayisi;i++) {
			 for(int j = 0;j<5;j++) { 
				 for(int k = 0 ;k<4;k++) { 
					 //bir �nceki eleman sonrakinden k���kse yerleri de�i�tiriliyor
					 if(getTakimInfo(maclar[i][k])[6] < getTakimInfo(maclar[i][k+1])[6] ) {
						 Mac[] tmp = maclar[i][k];
						 maclar[i][k] = maclar[i][k+1];
						 maclar[i][k+1] = tmp;

						 //ayn� i�lem s�ralama isimleri i�in de yap�l�yor
						 String tmpTakim = siraliTakimlar[i][k];
						 siraliTakimlar[i][k] = siraliTakimlar[i][k + 1];
						 siraliTakimlar[i][k + 1] = tmpTak�m;
					 }
				 }  
			 } 
		 }
		 
	
		//konsola hafta hafta liderlik tablosu yazd�rl�yor
		for(int i=1; i<= haftaSayisi; i++) {
			System.out.println("-------------------------------------------------------------");
			System.out.println(i +".Hafta		M	G	B	Y	A	Y	T");
			//hafta numaras�, ma� say�s�, galibiyet, beraberlik, yenilgi, at�lan gol, yiyilen gol, toplam puan
			 
			//5 tak�m oldu�undan d�ng� 5 kez tekrar edecek
			for(int j = 0; j<5; j++) {
				//tak�m bilgileri ti de�i�kenine atan�yor
				int[] ti = getTakimInfo(maclar[i][j]);
				
				//yukar�da verilen s�rayla bilgiler ekrana yazd�r�l�yor
				System.out.print(siraliTakimlar[i][j] + "	");	//tak�m ismi
				System.out.print(ti[0] + "	");
				System.out.print(ti[1] + "	");
				System.out.print(ti[2] + "	");
				System.out.print(ti[3] + "	");
				System.out.print(ti[4] + "	");
				System.out.print(ti[5] + "	");
				System.out.println(ti[6] + "	");
			}
		}
	}
	
	//tak�m verilerini alma fonksiyonu
	public static int[] getTakimInfo(Mac m[]) {
		int rakipSkor,takimSkor;
		int[] degerler = {0,0,0,0,0,0,0};
		int sayac = 0;
		
		//m dizisinde ba�lang��ta 99 adet mac tipinde obje var ve hepsi null de�erinde
		//metin dosyas�ndan okunup atanan de�i�kenlerin de�eri null olmayaca��ndan
		//null de�erine gelene kadar i�lem yap�l�yor
		while(m[sayac] != null) {
			
			//ma� sonucu skor al�n�yor ve degerler[4] de�i�kenine ekleniyor
			takimSkor = m[sayac].takimSkoruAl();
			degerler[4] += takimSkor;	//toplam skor
			
			degerler[0] += 1;			//toplam ma� say�s�
			rakipSkor = m[sayac].rakipSkoruAl();
			degerler[5] += rakipSkor;	//toplam yiyilen gol
			
			//ma� yenilgiyse toplam yenildi de�i�kenine 1 eklenir
			if(m[sayac].durumAl() == -1) {
				degerler[3] += 1;	//toplam yenilgi
			}
			//ma� durumu galibiyetse toplam galibiyetlere 1 eklenir
			//toplam skora 3 eklenir
			else if(m[sayac].durumAl() == 1) {
				degerler[1] += 1;	//toplam galibiyet
				degerler[6] += 3;	//toplam skor
			}
			//ma� durumu berabereyse toplam beraberlik de�i�keni 1 art�r�l�r
			//toplam skora 1 eklenir
			else {
				degerler[2] += 1;	//toplam beraberlik
				degerler[6] += 1;	//toplam skor
			}
			sayac++;				//saya� de�i�keni bir sonraki ma�� inceleyebilmek i�in 1 art�r�l�r
		}
		return degerler;
	}
}

