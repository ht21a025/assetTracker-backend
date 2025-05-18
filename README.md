# Asset Tracker Backend

資産管理アプリのバックエンド（Spring Boot）です。

## 使用技術

- Java 21
- Spring Boot – Webアプリケーションフレームワーク
- Spring Security – 認証とパスワードハッシュ
- Spring Data JPA – データベース操作
- PostgreSQL – データベース（Render 上）
- Render – デプロイ＆ホスティング
- Gradle – ビルドツール
- Docker – 本番用イメージビルド・デプロイ対応

## エンドポイント

| メソッド | パス                          | 説明                     |
|----------|-------------------------------|--------------------------|
| POST     | `/api/user/register`          | ユーザー登録             |
| POST     | `/api/user/login`             | ログイン（認証）         |
| POST     | `/api/asset/initial`          | 初期資産の登録           |
| POST     | `/api/asset/record`           | 月ごとの収支登録         |
| GET      | `/api/asset/summary?userId=1` | ユーザーの資産推移取得   |

## CORS 許可

以下のフロントエンドからのアクセスを許可しています：

```java
@CrossOrigin(origins = {
  "http://localhost:3000",
  "https://asset-tracker-frontend.vercel.app/"
})
```

## デプロイ環境
- Render（Web Service + PostgreSQL）
- ビルド：Gradle
- application.properties は Render の環境変数で設定

## ローカル起動手順
```bash
# ビルド
./gradlew build
# 起動
./gradlew bootRun
```
