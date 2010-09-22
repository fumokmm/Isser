package isser.util

class Prop {
  def propName
  def propFile
  def prop

  Prop(propName) {
    this.propName = propName
    this.propFile = new File(this.propName)
    this.prop = new Properties()
    load()
  }
  def load() {
    if (! propFile.canRead()) store()
    propFile.withInputStream{ prop.loadFromXML(it) }
  }
  def store() {
    propFile.withOutputStream{ prop.storeToXML(it, 'This is Isser\'s properies file.', 'UTF-8') }
  }

  def get(key) { prop.getProperty(key) }
  def get(key, defaultValue) { prop.getProperty(key, defaultValue) }
  def set(key, value) { prop.setProperty(key, value) }
}
