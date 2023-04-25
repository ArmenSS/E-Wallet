package com.wallet.mailservice.mapper;

import com.wallet.mailservice.dto.MailHistoryDto;
import com.wallet.mailservice.entity.MailHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MailHistoryMapper extends EntityMapper<MailHistoryDto, MailHistory> {
}
