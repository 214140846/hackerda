package com.hackerda.platform.domain.community;

import com.hackerda.platform.domain.student.StudentAccount;

import java.util.List;

public interface PosterRepository {

    StudentPoster findByStudentAccount(StudentAccount studentAccount);

    StudentPoster findStudentPosterByUserName(String userName);

    void save(PostBO postBO);

    PostDetailBO findByPostById(long id);

    List<PostDetailBO> findShowPost(int start, int count);
}