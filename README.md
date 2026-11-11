# Yaban Hayatı — Minecraft 1.21.1 (NeoForge) hayvan modu

Oyuna 68 yeni hayvan ekler: aslan, kaplan, fil, zürafa, zebra, gergedan, su aygırı, ayı, geyik,
goril, penguen, flamingo, kartal, baykuş, köpekbalığı, balina, yılan, yengeç ve daha fazlası.

## Kurulum (oynamak için)
1. Minecraft **1.21.1** için **NeoForge 21.1.x** kur.
2. `build/libs/yabanhayati-1.1.1.jar` dosyasını `.minecraft/mods` klasörüne at.
3. Yaratıcı modda **Yaban Hayatı** sekmesinde tüm çağırma yumurtaları var; hayatta kalma modunda
   hayvanlar kendi biyomlarında doğar (yeni keşfedilen bölgelerde).

## Özellikler
- Her hayvanın kendi modeli, dokusu, canı, hızı, ganimeti ve biyomu var.
- Yiyecekle çiftleştirme ve yavrular (ör. aslan → et, geyik → buğday/havuç, penguen → balık).
- Avcılar (aslan, kaplan, sırtlan...) av hayvanlarını kovalar; avlar kaçar.
- Timsah, köpekbalığı ve pirana oyuncuya saldırır; ayı, fil, gergedan gibi hayvanlar yalnızca vurulunca.
- Kartal, baykuş, karga, martı gibi kuşlar uçar. Yılan ve komodo ejderi zehirler.
- Her hayvan kendi etini düşürür (ör. Çiğ Aslan Eti). Etler fırında, tütsüleyicide ve kamp ateşinde pişer;
  yanarak ölen hayvan doğrudan pişmiş et verir.
- Etin doyuruculuğu hayvanın vahşiliğine göre artar: pişmiş geyik eti 4 açlık puanı doldururken
  pişmiş aslan eti 8, timsah ve köpekbalığı eti 9 doldurur. Büyük hayvanlar daha çok et düşürür.

## Yeni hayvan eklemek
Tüm hayvanlar tek dosyada: `src/defs/java/com/yabanhayati/def/Animals.java`.
Bir satır ekle, sonra:

```bash
./gradlew genAssets build
```

`genAssets` dokuları, dil dosyalarını (TR/EN), ganimet tablolarını ve doğma kurallarını yeniden üretir;
`build/onizleme/` klasörüne de tüm hayvanların önizleme resimlerini çıkarır.

Denemek için: `./gradlew runClient`
