import React from 'react';
import { StyleSheet } from 'react-native';
import { BaseToast, ErrorToast } from 'react-native-toast-message';
import { tokens } from '~/commons/styles/theme';

export const toastConfig = {
  success: (props: any) => (
    <BaseToast
      {...props}
      style={[
        styles.base,
        { borderLeftColor: tokens.colors.status.success },
      ]}
      contentContainerStyle={styles.content}
      text1Style={styles.text1}
      text2Style={styles.text2}
    />
  ),

  info: (props: any) => (
    <BaseToast
      {...props}
      style={[
        styles.base,
        { borderLeftColor: tokens.colors.status.warning },
      ]}
      contentContainerStyle={styles.content}
      text1Style={styles.text1}
      text2Style={styles.text2}
    />
  ),

  error: (props: any) => (
    <ErrorToast
      {...props}
      style={[
        styles.base,
        { borderLeftColor: tokens.colors.status.error },
      ]}
      contentContainerStyle={styles.content}
      text1Style={styles.text1}
      text2Style={styles.text2}
    />
  ),
};

const styles = StyleSheet.create({
  base: {
    backgroundColor: tokens.colors.surface,
    borderLeftWidth: 6,
    borderRadius: tokens.radius.card,
    minHeight: 64,
  },

  content: {
    paddingHorizontal: 16,
  },

  text1: {
    fontSize: 15,
    fontFamily: 'Inter-SemiBold',
    color: tokens.colors.text.primary,
  },

  text2: {
    fontSize: 13,
    fontFamily: 'Inter-Regular',
    color: tokens.colors.text.secondary,
  },
});
