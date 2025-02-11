//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.openplatform.bluetooth;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IResponse extends IInterface {
    void onResponse(int var1, Bundle var2) throws RemoteException;

    abstract class Stub extends Binder implements IResponse {
        private static final String DESCRIPTOR = "com.inuker.bluetooth.library.IResponse";
        static final int TRANSACTION_onResponse = 1;

        public Stub() {
            this.attachInterface(this, "com.inuker.bluetooth.library.IResponse");
        }

        public static IResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("com.inuker.bluetooth.library.IResponse");
                return iin != null && iin instanceof IResponse ? (IResponse)iin : new Proxy(obj);
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface("com.inuker.bluetooth.library.IResponse");
                    int _arg0 = data.readInt();
                    Bundle _arg1;
                    if (0 != data.readInt()) {
                        _arg1 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }

                    this.onResponse(_arg0, _arg1);
                    reply.writeNoException();
                    if (_arg1 != null) {
                        reply.writeInt(1);
                        _arg1.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 1598968902:
                    reply.writeString("com.inuker.bluetooth.library.IResponse");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IResponse {
            private final IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.inuker.bluetooth.library.IResponse";
            }

            public void onResponse(int code, Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.inuker.bluetooth.library.IResponse");
                    _data.writeInt(code);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        data.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }
}
