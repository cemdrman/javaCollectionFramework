# Katkı Rehberi

Bu proje Java için Türkçe kaynak oluşturmak amacıyla hazırlanmıştır. Hata düzeltmeleri, yeni
örnekler, daha anlaşılır açıklamalar ve yazım düzeltmeleri memnuniyetle karşılanır.

## Başlamadan önce

- **JDK 25** veya üzeri kurulu olmalıdır.
- Değişiklik yapmadan önce projenin derlendiğinden emin olun:

```bash
./mvnw verify
```

## Yeni bir örnek eklemek

1. Örneği konusuna uygun pakete ekleyin (ör. `collections/map`, `eszamanli`, `stream`).
2. Sınıf adı `XxxOrnegi` biçiminde olsun ve bir `main` metodu içersin.
3. Sınıfın başına, anlattığı yapıyı özetleyen bir Javadoc yazın: ne işe yarar, iç yapısı nasıldır,
   temel işlemlerin karmaşıklığı nedir, ne zaman tercih edilmelidir.
4. Çıktıları `javacollectionframework.ortak.Konsol` sınıfındaki `baslik`, `bolum` ve `yaz`
   metotlarıyla biçimlendirin.
5. Çıktı **her çalıştırmada aynı** olmalı. Rastgelelik gerekiyorsa sabit tohumlu `Random`
   kullanın; sırası tanımsız yapıları (`HashSet`, `Set.of`...) bunu bilerek gösterin.
6. Örneği `javacollectionframework.Calistir` menüsüne ekleyin.
7. İlgili `docs/` dokümanına kısa bir anlatım ve örneğe bağlantı ekleyin.

## Kod stili

- Değişken, metot ve sınıf adları Türkçe, ancak Türkçe karakter içermeden yazılır
  (`sehirler`, `enBuyukK`). Açıklamalar ve çıktılar Türkçe karakterlerle yazılır.
- Ham tip (raw type) kullanmayın; generic tipleri her zaman belirtin.
- Derleme `-Xlint:all` ile **uyarısız** olmalıdır.
- Girinti 4 boşluktur (bkz. `.editorconfig`).

## Commit mesajları

[Conventional Commits](https://www.conventionalcommits.org/) biçimi kullanılır:

```text
feat(map): ConcurrentSkipListMap örneği ekle
fix(list): ArrayList örneğindeki döngü hatasını düzelt
docs: Set dokümanına EnumSet bölümü ekle
```

## Pull request

1. Depoyu fork'layın ve `master`'dan yeni bir dal açın.
2. `./mvnw verify` komutunun başarılı olduğundan emin olun.
3. Değişikliğinizi ve nedenini açıklayan bir pull request açın.
