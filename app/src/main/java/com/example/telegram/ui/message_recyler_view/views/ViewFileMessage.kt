package com.example.telegram.ui.message_recyler_view.views

data class ViewFileMessage(
    override val id: String,
    override val from: String,
    override val timeStamp: String,
    override val fileUrl: String,
    override val text: String = ""
) : MessageView {
    override fun getTypeView(): Int {
        return MessageView.MESSAGE_FILE
    }

    override fun equals(other: Any?): Boolean {//// boshqa klass yoki interfaceni Any tiplisini solishtiradi
        return (other as MessageView).id == id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + from.hashCode()
        result = 31 * result + timeStamp.hashCode()
        result = 31 * result + fileUrl.hashCode()
        result = 31 * result + text.hashCode()
        return result
    }
}