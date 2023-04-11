export function UserMergeDtoFromJSONTyped(json: any, ignoreDiscriminator: boolean): UserMergeDto {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {

        'contacts': (json['contacts'] === null ? null : (json['contacts'] as Array<any>).map(ContactDtoFromJSON)),
        'isRequested': json['isRequested'],
        'hasSameEmail': json['hasSameEmail'],
        'isOwnPartner': json['isOwnPartner'],
        'isDirectMerge': json['isDirectMerge'],
        'hasEmail': json['hasEmail'],
        'partnerIdCol': json['partnerIdCol'],
    };
}

export function UserMergeDtoToJSON(value?: UserMergeDto | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {

        'contacts': ((value.contacts as Array<any>).map(ContactDtoToJSON)),
        'isRequested': value.isRequested,
        'hasSameEmail': value.hasSameEmail,
        'isOwnPartner': value.isOwnPartner,
        'isDirectMerge': value.isDirectMerge,
        'hasEmail': value.hasEmail,
        'partnerIdCol': value.partnerIdCol,
    };
}


