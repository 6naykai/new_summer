package com.nbu.service;

import com.nbu.util.Result;

public interface UserBanService {

    Result GetBanUserListLimitTenPages(Integer startPage);
    Result BanUser(String username);

    Result ReleaseUser(String username);
}
