package com.mobile.aa.survey.form.model

class Form{
    public val TYPE_SHORT = 1
    public val TYPE_LONG = 2
    public val TYPE_DATE = 21
    public val TYPE_TIME = 22
    public val TYPE_MULTIPLE = 91
    public val TYPE_CHECKBOX = 92
    public val TYPE_DROPDOWN = 93
    public val TYPE_FILE = 51
    var type: Int = 0
    var formType: HashMap<Int, String>
    var list: ArrayList<FormItem> = ArrayList()
    var question: String = ""
    var required: Boolean = false
    var answer: String = ""
    init {
        formType = HashMap()
        formType.put(TYPE_SHORT, "Text Short")
        formType.put(TYPE_LONG, "Text Long")
        formType.put(TYPE_DATE, "Date")
        formType.put(TYPE_TIME, "Time")
        formType.put(TYPE_MULTIPLE, "Multiple Choice")
        formType.put(TYPE_CHECKBOX, "Checkbox")
        formType.put(TYPE_DROPDOWN, "Dropdown")
        formType.put(TYPE_FILE, "File")
    }
}

