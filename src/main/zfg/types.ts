type StringBuilder = {}
type Unit = {}


type Kind =
  'UNK' | 'ERR' |
  'BIT' | 'U08' | 'U16' | 'U32' | 'U64' | 'I08' | 'I16' | 'I32' | 'I64' | 'F32' | 'F64' |
  'ARR' | 'TUP' | 'REC' |
  'FUN' |
  'NOM'

type Type = {
  kind: Kind,
  toString: (buf: StringBuilder | Unit, seen: Set<Type> | Unit) => string,
  equals: (other: Type) => boolean,
  hashCode: () => number,
}

let Bit: Type = {
  kind: 'BIT',
  toString: (buf, seen) => 'Bit',
  equals: other => other.kind === 'BIT',
  hashCode: () => 1,
}


