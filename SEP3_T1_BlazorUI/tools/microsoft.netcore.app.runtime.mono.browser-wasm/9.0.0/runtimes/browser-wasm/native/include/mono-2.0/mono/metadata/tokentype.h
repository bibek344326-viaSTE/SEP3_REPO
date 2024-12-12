/**
 * \file
 */

#ifndef _MONO_METADATA_TOKENTYPE_H_
#define _MONO_METADATA_TOKENTYPE_H_

/*
 * These tokens match the table ID except for the last
 * three (string, name and base type which are special)
 */

typedef enum {
	MONO_TOKEN_MODULE            = 0x00000000,
	MONO_TOKEN_TYPE_REF          = 0x01000000,
	MONO_TOKEN_TYPE_DEF          = 0x02000000,
	MONO_TOKEN_FIELD_DEF         = 0x04000000,
	MONO_TOKEN_METHOD_DEF        = 0x06000000,
	MONO_TOKEN_PARAM_DEF         = 0x08000000,
	MONO_TOKEN_INTERFACE_IMPL    = 0x09000000,
	MONO_TOKEN_MEMBER_REF        = 0x0a000000,
	MONO_TOKEN_CUSTOM_ATTRIBUTE  = 0x0c000000,
	MONO_TOKEN_PERMISSION        = 0x0e000000,
	MONO_TOKEN_SIGNATURE         = 0x11000000,
	MONO_TOKEN_EVENT             = 0x14000000,
	MONO_TOKEN_PROPERTY          = 0x17000000,
	MONO_TOKEN_MODULE_REF        = 0x1a000000,
	MONO_TOKEN_TYPE_SPEC         = 0x1b000000,
	MONO_TOKEN_ASSEMBLY          = 0x20000000,
	MONO_TOKEN_ASSEMBLY_REF      = 0x23000000,
	MONO_TOKEN_FILE              = 0x26000000,
	MONO_TOKEN_EXPORTED_TYPE     = 0x27000000,
	MONO_TOKEN_MANIFEST_RESOURCE = 0x28000000,
	MONO_TOKEN_GENERIC_PARAM     = 0x2a000000,
	MONO_TOKEN_METHOD_SPEC       = 0x2b000000,

	/*
	 * These do not match metadata tables directly
	 */
	MONO_TOKEN_STRING            = 0x70000000,
	MONO_TOKEN_NAME              = 0x71000000,
	MONO_TOKEN_BASE_TYPE         = 0x72000000
} MonoTokenType;

#endif /* _MONO_METADATA_TOKENTYPE_H_ */
