# HyperLogLog Cardinality Estimation (Java)

## Proje Açıklaması

Bu proje, büyük veri analitiğinde kullanılan olasılıksal veri yapılarından biri olan **HyperLogLog (HLL)** algoritmasının Java dili ile sıfırdan gerçeklenmesini amaçlamaktadır.

HyperLogLog algoritması, çok büyük veri kümelerinde **benzersiz eleman sayısını (cardinality)** tahmin etmek için kullanılan bir algoritmadır. Geleneksel yöntemlerde tüm verinin saklanması gerekirken HyperLogLog çok küçük bir bellek kullanarak yaklaşık sonuçlar üretir.

Bu nedenle Google BigQuery, Redis ve Apache Spark gibi büyük veri sistemlerinde kullanılmaktadır.

---

# Algoritmanın Çalışma Mantığı

HyperLogLog algoritması aşağıdaki temel adımlardan oluşmaktadır.

## 1. Hash Fonksiyonu

Veri öncelikle bir hash fonksiyonundan geçirilir.

Amaç:
- Veriyi rastgele dağıtmak
- Bucketlara dengeli şekilde yerleştirmek

Bu projede 64 bit hash üretimi kullanılmaktadır.

---

## 2. Bucketing (Kovalama)

Hash değerinin ilk **p biti** bucket numarasını belirler.


m = 2^p


Burada

- **p** : bucket bit sayısı
- **m** : bucket sayısı

Örnek:

| p | bucket sayısı |
|---|---|
| 8 | 256 |
| 10 | 1024 |
| 12 | 4096 |

---

## 3. Leading Zero Count

Hash değerinin kalan bitleri incelenir ve **ardışık sıfır sayısı** hesaplanır.

Bu değer veri kümesinin büyüklüğü hakkında istatistiksel bir bilgi sağlar.

Her bucket için bir **register dizisi** tutulur ve en büyük değer saklanır.

---

## 4. Cardinality Tahmini

Veri kümesinin büyüklüğü şu formül ile hesaplanır:


E = α * m² / Σ(2^-register)


Burada

- **E** : tahmini cardinality
- **m** : bucket sayısı
- **α** : algoritma sabiti

---

## 5. Düzeltme Mekanizmaları

Algoritmada doğruluğu artırmak için iki farklı düzeltme uygulanır.

### Küçük veri setleri
Linear counting uygulanır.

### Büyük veri setleri
Large range correction uygulanır.

---

## 6. Merge Özelliği

HyperLogLog yapıları birleştirilebilir.

İki farklı HLL şu şekilde birleştirilir:


register[i] = max(register1[i], register2[i])


Bu özellik özellikle **dağıtık veri sistemleri** için önemlidir.

---

# Teorik Hata Analizi

HyperLogLog algoritmasının tahmini hata oranı:


Error ≈ 1.04 / √m


Burada

- **m** bucket sayısını temsil eder.

| Bucket sayısı | Hata oranı |
|---|---|
| 1024 | %3.25 |
| 4096 | %1.6 |
| 16384 | %0.8 |

---

# Proje Yapısı


hyperloglog-java

src
├── HyperLogLog.java
├── Main.java
└── MergeTest.java


Dosyalar:

### HyperLogLog.java

Algoritmanın ana implementasyonunu içerir.

### Main.java

Algoritmanın test edilmesini sağlar ve gerçek değer ile tahmini değeri karşılaştırır.

### MergeTest.java

İki farklı HyperLogLog yapısının nasıl birleştirildiğini gösterir.

---

# Kullanılan Teknolojiler

- Java
- IntelliJ IDEA
- GitHub
- Büyük Dil Modelleri (ChatGPT / Gemini)

Kod geliştirme sürecinde **Agentic Coding yaklaşımı** kullanılmıştır.

---

# Örnek Çalıştırma

Program çalıştırıldığında aşağıdaki gibi bir çıktı elde edilir:


Gerçek Cardinality : 95012
HLL Tahmini : 96780


Tahmin edilen değer gerçek değere oldukça yakındır.

---

# Sonuç

HyperLogLog algoritması büyük veri sistemlerinde çok az bellek kullanarak büyük veri kümelerinin büyüklüğünü tahmin etmek için kullanılan güçlü bir olasılıksal veri yapısıdır.

Bu projede algoritmanın temel bileşenleri Java dili kullanılarak sıfırdan gerçeklenmiştir.
