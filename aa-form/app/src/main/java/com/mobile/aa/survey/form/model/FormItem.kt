package com.mobile.aa.survey.form.model

class FormItem {
    var value: String = ""
    var TYPE_ADD = 0
    var TYPE_EDIT = 1
    var type: Int = TYPE_ADD
    var answer: String = ""

    override fun toString():String {
        return value
    }
}