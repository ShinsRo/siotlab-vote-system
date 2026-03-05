package com.siotman.vote.core.policy.domain.spec

import com.siotman.vote.core.common.json.CoreJson

object VotePolicySpecSerde {
    private val supportedTypes: Map<String, Class<out VotePolicySpec>> = listOf(
        ChoicePolicy::class.java,
        SingleChoicePolicy::class.java,
        MultipleChoicePolicy::class.java,
        YesNoPolicy::class.java,
        CompositePolicy::class.java,
    ).associateBy { requireNotNull(it.simpleName) { "정책 타입 simpleName은 null일 수 없습니다." } }

    fun typeOf(spec: VotePolicySpec): String =
        requireNotNull(spec::class.java.simpleName) { "정책 타입 simpleName은 null일 수 없습니다." }

    fun paramsOf(spec: VotePolicySpec): String = CoreJson.objectMapper.writeValueAsString(spec)

    fun deserialize(type: String, params: String): VotePolicySpec {
        val clazz = supportedTypes[type]
            ?: throw IllegalArgumentException("지원하지 않는 정책 타입입니다. type=$type")
        return CoreJson.objectMapper.readValue(params, clazz)
    }
}
