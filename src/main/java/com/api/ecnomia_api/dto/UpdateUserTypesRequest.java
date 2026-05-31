package com.api.ecnomia_api.dto;

import com.api.ecnomia_api.enums.UserType;
import java.util.Set;

public record UpdateUserTypesRequest(
    Set<UserType> tipos
) {}
