package com.siotman.vote.candidate.application

class CandidateNotFoundException(id: Long) : RuntimeException("후보를 찾을 수 없습니다. id=$id")
