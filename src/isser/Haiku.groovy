package isser

import hatenahaiku4j.*

class Haiku {
  static {
//    System.setProperty 'http.proxyHost', 'プロキシのホスト'
//    System.setProperty 'http.proxyPort', '8080'
  }

  static def userId = 'xxxxxxx'
  static def password = 'ppppppp'
  static def applicationName = 'Isser'

  static def loginUser = new LoginUser(userId, password, applicationName)
  static def api = new HatenaHaikuAPI(loginUser);

  // タイムライン系メソッド
  static def getPublicTimeline() { api.getPublicTimeline() }
  static def getKeywordTimeline(keyword) { api.getKeywordTimeline(keyword) }

  // 投稿系メソッド
  static def entry(text) { api.entry(text) }
}
