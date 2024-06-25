package com.natiqhaciyef.common.model

enum class QuestionCategories(val title: String) {
    ALL("All"),
    GENERAL("General"),
    ACCOUNT("Account"),
    SERVICE("Service"),
    FILES_RELATED("Files & Process");

    companion object {
        fun stringToType(title: String): QuestionCategories =
            when (title) {
                ALL.title, ALL.name, ALL.name.lowercase() -> { ALL }
                GENERAL.title, GENERAL.name, GENERAL.name.lowercase() -> { GENERAL }
                ACCOUNT.title, ACCOUNT.name, ACCOUNT.name.lowercase() -> { ACCOUNT }
                SERVICE.title, SERVICE.name, SERVICE.name.lowercase() -> { SERVICE }
                FILES_RELATED.title, FILES_RELATED.name, FILES_RELATED.name.lowercase() -> { FILES_RELATED }
                else -> { ALL }
            }

        fun makeQuestionTypeList(): List<QuestionCategories>{
            return listOf(ALL, GENERAL, ACCOUNT, SERVICE, FILES_RELATED)
        }

        fun makeQuestionTypeStringList(): List<String>{
            return makeQuestionTypeList().map { it.title }
        }
    }
}