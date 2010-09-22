package isser

import groovy.swing.SwingBuilder
import groovy.xml.MarkupBuilder
import javax.swing.*
import java.awt.*
import java.awt.BorderLayout as BL

import hatenahaiku4j.util.*
import isser.component.*
import isser.util.*

class Isser {
  static void main(String[] args) {
    def isser = new Isser()
    isser.show()
  }

  def show() {
    def swingBuilder = new SwingBuilder()

    // メニューバー
    def customMenuBar = {
      swingBuilder.menuBar{

        // File Menu
        menu(text: 'File', mnemonic: 'F') {
          menuItem(text: 'Exit', mnemonic: 'X', actionPerformed: { dispose() })
        }

        // Setting Menu
        menu(text: 'Setting', mnemonic: 'S') {
          menuItem(text: 'General', mnemonic: 'G', actionPerformed: {
            def prop = new Prop('./isser.properties.xml')
            swingBuilder.dialog(id: 'dialog1', owner: this, title: '設定ダイアログ',
                                show: true, pack: true, modal: true) {
              panel(constraints: BL.NORTH) {
                propA = textField(columns: 15, text: prop.get('a', ''))
                propB = textField(columns: 15, text: prop.get('b', ''))
              }
              panel(constraints: BL.SOUTH) {
                button(text: 'ぼたん', actionPerformed: {
                  prop.set('a', propA.text)
                  prop.set('b', propB.text)
                  prop.store()
                  dialog1.dispose()
                })
              }
            }
          })
        }

      }
    }

    // 検索パネル
    def searchPanel = {
      swingBuilder.panel(constraints: BL.NORTH) {
        searchField = textField(columns: 15)
        searchButton = button(text: 'search', actionPerformed: {
          doOutside {
            searchField.disable()
            searchButton.disable()

            def result = searchField.text ?
              Haiku.getKeywordTimeline(searchField.text) :
              Haiku.publicTimeline

            resultsList.listData = result.collect(toTimelineHtml)

            searchButton.enable()
            searchField.enable()
          }
        })

        haikuButton = button(text: 'Haiku!', actionPerformed: {
          doOutside {
            searchField.disable()
            haikuButton.disable()

            if (searchField.text) {
              Haiku.entry(searchField.text)
              searchField.text = '' // 初期化
            }

            haikuButton.enable()
            searchField.enable()
          }
        })
      }
    }

    // 結果パネル
    def resultsPanel = {
      swingBuilder.scrollPane(constraints: BL.CENTER) {
        resultsList = list(fixedCellWidth: 380, fixedCellHeight: 75, cellRenderer: new StripeRenderer())
      }
    }

    def title = "Isser ${Class.class.getResourceAsStream('/version.txt').text}"
    swingBuilder.frame(title: title,
                       defaultCloseOperation: JFrame.EXIT_ON_CLOSE,
                       size: [400, 500],
                       show: true) {
      customMenuBar() // メニューバー
      searchPanel()   // 検索パネル
      resultsPanel()  // 結果パネル
    }
  }

  // タイムライン表示用HTMLに変換
  def toTimelineHtml = { status ->
    def writer = new StringWriter()
    new MarkupBuilder(writer).html {
      body {
        div {
          b(style: 'color:blue', 'font-weight': 'bold', "【${status.keyword}】")
        }
        div {
          mkp.yieldUnescaped("${status.text.replaceAll(/\r?\n/, '<br />')}")
        }
        div {
          span(style: 'color:red'    ,"at ${HatenaUtil.formatDate(status.createdAt)}")
          span(style: 'color:green'  ," by ${status.userId}"                         )
          span(style: 'color:purple' ," from ${status.source}"                       )
        }
      }
    }
    writer.toString()
  }


}
