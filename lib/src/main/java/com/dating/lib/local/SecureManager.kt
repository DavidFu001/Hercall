package com.dating.lib.local

class SecureManager {
    companion object {
        /**
         * 录屏白名单
         *  - 白名单页面（包含其次级页面）
         *  - 用户个人中心页
         */
        private val whiteActivityNames = mutableListOf<String>()

        fun getWhiteActivityLists(): MutableList<String> {
            if (whiteActivityNames.isEmpty()) {
                initWhiteList()
            }
            return whiteActivityNames
        }


        private fun initWhiteList() {
//            whiteActivityNames.add(FeedbackActivity::class.java.simpleName)
//            whiteActivityNames.add(BlockListActivity::class.java.simpleName)
        }
    }


}